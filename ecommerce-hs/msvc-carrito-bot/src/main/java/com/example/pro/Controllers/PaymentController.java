package com.example.pro.Controllers;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.services.IPaymentService;

import com.mercadopago.resources.Payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pago")

public class PaymentController {

	static final String MP_WEBHOOK_SECRET = "1f3041fd55bced962ec42376f1137039b42a58b5c6a1958e5d85cabb7d6f260a";
	@Autowired
	private IPaymentService iPaymentService;

	@PostMapping("/crear-preferencia")
	public ResponseEntity<?> crearPreferencia(@RequestBody VentaAndDetalles venta) {
		return iPaymentService.crearPreferencia(venta);
	}

	@PostMapping
	public ResponseEntity<String> recibirWebhook(@RequestParam("id") Long paymentId, @RequestParam String topic) {

		if ("payment".equalsIgnoreCase(topic)) {
			try {
				Payment payment = Payment.findById(paymentId.toString());
				System.out.println("estado del pago:" + payment.getStatusDetail());
				String statusPayment = payment.getStatus().name().toString();
				switch (statusPayment.toLowerCase()) {
				case "approved": {
					iPaymentService.generarVentaConMercadoPago(payment);
					break;
				}
				case "refunded": {
					iPaymentService.cancelarVenta(payment);
					break;
				}
				default:
					throw new IllegalArgumentException("value: " + statusPayment);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error al procesar notificacion de Mercado pago");
			}
		}

		return ResponseEntity.ok("Webhook recibido");
	}

	@PostMapping(value = "/prueba", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> recibirPago(@RequestBody Map<String, Object> body) {
		System.out.println("Webhook prueba recibido: " + body);
		try {
			 Map<String, Object> data = (Map<String, Object>) body.get("data");
			    Long paymentId = Long.valueOf(data.get("id").toString());
			Payment payment = Payment.findById(paymentId.toString());
			System.out.println("estado del pago:" + payment.getStatusDetail());
			String statusPayment = payment.getStatus().name().toString();
			switch (statusPayment.toLowerCase()) {
			case "approved": {
				iPaymentService.generarVentaConMercadoPago(payment);
				break;
			}
			case "refunded": {
				iPaymentService.cancelarVenta(payment);
				break;
			}
			default:
				throw new IllegalArgumentException("value: " + statusPayment);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al procesar notificacion de Mercado pago");
		}

		return ResponseEntity.ok("Webhook recibido");
	}

}
