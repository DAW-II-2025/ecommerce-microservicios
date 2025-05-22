package com.example.pro.Controllers;

import java.time.LocalDate;
import java.util.Map;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.DTO.PaymentIntentDTO;
import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.DTO.VentaDTO;
import com.example.pro.model.Cliente;
import com.example.pro.model.Producto;
import com.example.pro.services.IClienteServices;
import com.example.pro.services.IPaymentService;
import com.example.pro.services.IProductoServices;
import com.example.pro.services.IUsuarioServices;
import com.example.pro.services.Impl.PaymentService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Address;
import com.mercadopago.resources.datastructures.preference.BackUrls;
import com.mercadopago.resources.datastructures.preference.Identification;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import com.mercadopago.resources.datastructures.preference.PaymentMethods;
import com.mercadopago.resources.datastructures.preference.Phone;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pago")

public class PaymentController {

    @Autowired
    private IPaymentService iPaymentService;

    @Autowired
    private IProductoServices iProductoServices;

    @Autowired
    private IUsuarioServices _IUsuarioServices;

    @PostMapping("/crear-preferencia")
    public ResponseEntity<?> crearPreferencia(@RequestBody VentaAndDetalles venta) {
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
		item.setTitle(pro.getDescripcion())
		.setPictureUrl(pro.getImagen())
		.setQuantity(detalle.getCant())
			.setUnitPrice(Float.parseFloat(pro.getPrecioUnidad() + ""));
		preference.appendItem(item);		
	    });
	    Gson gsson = new Gson();
	    JsonObject metadata = gsson.toJsonTree(venta).getAsJsonObject();
	    preference.setMetadata(metadata);
	    
//	    BackUrls backUrls = new BackUrls();
//	    backUrls.setSuccess("http://localhost:3000/verMisPedidos")
//		    .setFailure("http://localhost:3000/comprar")
//		    .setPending("http://localhost:3000/verMisPedidos");
	    BackUrls backUrls = new BackUrls();
	    backUrls.setSuccess("https://proyectocarritoantonitrejo.netlify.app/verMisPedidos")
	    .setFailure("https://proyectocarritoantonitrejo.netlify.app/comprar")
	    .setPending("https://proyectocarritoantonitrejo.netlify.app/verMisPedidos");

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
    

//  @PostMapping("/crear-preferencia")
//  public ResponseEntity<?> crearPreferencia(@RequestBody VentaAndDetalles venta,
//                                            HttpServletRequest request,
//                                            HttpServletResponse response) {
//    try {
//      // Aseguramos que la sesión existe
//      request.getSession(true);
//
//      Preference preference = new Preference();
//      PaymentMethods payMethods = new PaymentMethods();
//      payMethods.setInstallments(1);
//      preference.setPaymentMethods(payMethods);
//      VentaDTO ventadto = venta.getVentaDTO();
//      ventadto.setCli(_IUsuarioServices.getUsuarioActual().getCorreo());
//      venta.setVentaDTO(ventadto);
//      venta.getDetallesDTO().forEach(detalle -> {
//        Producto pro = iProductoServices.getById(detalle.getProducto());
//        Item item = new Item();
//        item.setTitle(detalle.getNomProd()).setQuantity(detalle.getCant())
//          .setUnitPrice(Float.parseFloat(pro.getPrecioUnidad() + ""));
//        preference.appendItem(item);
//      });
//      Gson gsson = new Gson();
//      JsonObject metadata = gsson.toJsonTree(venta).getAsJsonObject();
//      preference.setMetadata(metadata);
//
//      BackUrls backUrls = new BackUrls();
//      backUrls.setSuccess("https://proyectocarritoantonitrejo.netlify.app/verMisPedidos")
//        .setFailure("https://proyectocarritoantonitrejo.netlify.app/comprar")
//        .setPending("https://proyectocarritoantonitrejo.netlify.app/verMisPedidos");
//
//      preference.setBackUrls(backUrls);
//      preference.setAutoReturn(Preference.AutoReturn.approved);
//
//      // Añadimos el ID de sesión a los metadatos para mejor seguimiento
//      if (metadata.get("sessionId") == null) {
//        metadata.addProperty("sessionId", request.getSession().getId());
//      }
//
//      Preference savedPreference = preference.save();
//      System.err.println("metadata: " + savedPreference.getMetadata());
//
//      // Aseguramos que la cookie de sesión se envíe con las directivas correctas
//      String sessionId = request.getSession().getId();
//      Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
//      sessionCookie.setPath("/");
//      sessionCookie.setSecure(true);
//      sessionCookie.setHttpOnly(true);
//      sessionCookie.setAttribute("SameSite", "None");
//      response.addCookie(sessionCookie);
//
//      return ResponseEntity
//        .ok(Map.of("id", savedPreference.getId(), "init_point", savedPreference.getInitPoint()));
//
//    } catch (Exception e) {
//      e.printStackTrace();
//      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la preferencia: " + e.getMessage());
//    }
//  }
    @PostMapping
    public ResponseEntity<String> recibirWebhook(@RequestParam("id") Long paymentId,
	    @RequestParam("topic") String topic) {

	if ("payment".equalsIgnoreCase(topic)) {
	    try {
		Payment payment = Payment.findById(paymentId.toString());
		System.out.println("estado del pago:" + payment.getStatusDetail());
		String statusPayment = payment.getStatus().name().toString();
		switch (statusPayment.toLowerCase()) {
		case "refunded": {
		    iPaymentService.generarVentaConMercadoPago(payment);
		    break;
		}
		case "approved": {
		    iPaymentService.cancelarVenta(payment);
		    break;
		}
		default:
		    throw new IllegalArgumentException("value: " + statusPayment);
		}

	    } catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar notificacion de Mercado ago");
	    }
	}

	return ResponseEntity.ok("Webhook recibido");
    }

}
