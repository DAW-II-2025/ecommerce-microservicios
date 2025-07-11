package com.example.pro.services.Impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.pro.DTO.PagoDTO;
import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.DTO.VentaDTO;

import com.example.pro.Repository.IPagoRepository;

import com.example.pro.model.EstadoPedido;

import com.example.pro.model.Pago;
import com.example.pro.model.Pedido;
import com.example.pro.model.Producto;
import com.example.pro.model.Venta;

import com.example.pro.services.IPaymentService;
import com.example.pro.services.IProductoServices;
import com.example.pro.services.IReporteServices;
import com.example.pro.services.IUsuarioServices;
import com.example.pro.services.IVentaServices;
import com.example.pro.services.IWhatsappServices;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.PaymentMethods;

import net.sf.jasperreports.engine.JRException;

@Service
public class PaymentService implements IPaymentService {

	private Logger log = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private IVentaServices iVentaServices;
	@Autowired
	private IPagoRepository _IPagoRepository;
	@Autowired
	private IReporteServices _IReporteServices;
	@Autowired
	private IWhatsappServices _IWhatsappServices;
	@Autowired
	private Gson gson;
	@Autowired
	private IProductoServices iProductoServices;
	@Autowired
	private IUsuarioServices _IUsuarioServices;

	@Value("${url.front.base}")
	private String urlFront;

	@Override
	public void generarVentaConMercadoPago(Payment payment) throws IOException, JRException {
		log.info("extraendo datos de payment de Mercado pago encontrado por id, del pagador: "
				+ payment.getPayer().getIdentification().getNumber());
		double monto = payment.getTransactionAmount().doubleValue();
		String metodo = payment.getPaymentMethodId();
		PagoDTO pago = new PagoDTO();
		pago.setMetodo(metodo);
		pago.setEstado(payment.getStatus().name());
		pago.setPaymentId(payment.getId().toString());
		String metadata = gson.toJson(payment.getMetadata());
		VentaAndDetalles venta = gson.fromJson(metadata, VentaAndDetalles.class);
		VentaDTO ventadto = venta.getVentaDTO();
		ventadto.setMonto(monto);
		ventadto.setFechaVenta(LocalDateTime.now().toString());
		venta.setVentaDTO(ventadto);
		venta.setPagoDTO(pago);
		Venta ventaSaved = new Venta();
		Optional<Pago> pagoOp = _IPagoRepository.findById(payment.getId());
		if (pagoOp.isEmpty()) {
			log.info("generando la venta con el pago: " + payment.getId());
			ventaSaved = iVentaServices.SaveVentaAndDetalles(venta);
			System.out.println("✅ Venta registrada: " + ventaSaved.getIdVenta());
		} else {
			ventaSaved = iVentaServices.findByPago(pagoOp.get());
		}		
		String tel = ventaSaved.getCli().getTelefono();
		// enviando comprobante de pago
		byte[] imageBytes = _IReporteServices.generarBoleta(ventaSaved);
		log.info("Tamaño del byte[] generado: {}", imageBytes.length);
		_IWhatsappServices.sendImage(imageBytes, tel);
		// enviando notificacion de pago
		String mensaje = String.format("💸 tu pago %s de S./ %.2f fue aprobado.\n 👍😊 ¡Gracias por su compra! 👍😊 ",
				pago.getPaymentId(), monto);
		_IWhatsappServices.sendMessage(mensaje, tel);

	}

	@Override
	public void cancelarVenta(Payment payment) {
		Pago pago = _IPagoRepository.findById(payment.getId()).orElseThrow();
		Venta venta = iVentaServices.findByPago(pago);
		Pedido pedido = venta.getPedido();
		pago.setEstado(payment.getStatus().name().toLowerCase());
		pedido.setEstado(EstadoPedido.CANCELADO);
		venta.setPago(pago);
		Venta ventaSaved = iVentaServices.SaveVenta(venta);
		log.info("cancelando la venta: " + ventaSaved.getIdVenta() + "con el pago: " + pago.getId());
		// enviando mensaje de cancelacion de pago
		String mensaje = (String.format("🚨🚫 tu pago %s de S./ %.2f fue devuelto", payment.getId(), venta.getMonto()));
		String tel = ventaSaved.getCli().getTelefono();
		_IWhatsappServices.sendMessage(mensaje, tel);
	}

	@Override
	public ResponseEntity<?> crearPreferencia(VentaAndDetalles venta) {
		try {
			Preference preference = new Preference();
			PaymentMethods payMethods = new PaymentMethods();
			payMethods.setInstallments(1);
			preference.setPaymentMethods(payMethods);
			VentaDTO ventadto = venta.getVentaDTO();
			ventadto.setCli(_IUsuarioServices.getUsuarioActual().getCorreo());
			venta.setVentaDTO(ventadto);
			venta.getDetallesDTO().forEach(detalle -> {
				Producto pro = iProductoServices.getById(detalle.getProducto());
				Item item = new Item();
				item.setTitle(pro.getDescripcion()).setPictureUrl(pro.getImagen()).setQuantity(detalle.getCant())
						.setUnitPrice(Float.parseFloat(pro.getPrecioUnidad() + ""));
				preference.appendItem(item);
			});
			Gson gsson = new Gson();
			JsonObject metadata = gsson.toJsonTree(venta).getAsJsonObject();
			preference.setMetadata(metadata);

			BackUrls backUrls = new BackUrls();
			backUrls.setSuccess(urlFront + "/verMisPedidos").setFailure(urlFront + "/comprar")
					.setPending(urlFront + "/verMisPedidos");

			preference.setBackUrls(backUrls);
			preference.setAutoReturn(Preference.AutoReturn.approved);

			Preference savedPreference = preference.save();

			return ResponseEntity
					.ok(Map.of("id", savedPreference.getId(), "init_point", savedPreference.getInitPoint()));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la preferencia");
		}
	}

}
