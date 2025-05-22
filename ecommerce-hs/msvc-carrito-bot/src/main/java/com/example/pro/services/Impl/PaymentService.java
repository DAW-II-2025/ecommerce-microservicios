package com.example.pro.services.Impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.example.pro.DTO.PagoDTO;
import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.DTO.VentaDTO;

import com.example.pro.Repository.IPagoRepository;

import com.example.pro.model.EstadoPedido;

import com.example.pro.model.Pago;
import com.example.pro.model.Pedido;
import com.example.pro.model.Venta;

import com.example.pro.services.IPaymentService;
import com.example.pro.services.IReporteServices;
import com.example.pro.services.IVentaServices;
import com.example.pro.services.IWhatsappServices;

import com.google.gson.Gson;
import com.mercadopago.resources.Payment;


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
	//enviando notificacion de pago
	String mensaje = String.format("💸 tu pago %s de S./ %.2f fue aprobado.\n 👍😊 ¡Gracias por su compra! 👍😊 ", pago.getPaymentId(), monto);
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
	String tel= ventaSaved.getCli().getTelefono();
	_IWhatsappServices.sendMessage(mensaje, tel);
    }

}
