package com.example.pro.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.services.IWhatsappServices;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/webhook")
public class WebhookMetaController {
	

	@Autowired
	private IWhatsappServices _DialogflowService;
	private static final String VERIFY_TOKEN = "verifica123";

	@GetMapping
	public ResponseEntity<String> verifyWebhook(@RequestParam(name = "hub.mode") String mode,
			@RequestParam(name = "hub.verify_token") String token,
			@RequestParam(name = "hub.challenge") String challenge) {

		if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
			return ResponseEntity.ok(challenge);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verificación fallida");
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> receiveWebhook(@RequestBody Map<String, Object> body) {
		return _DialogflowService.webhookMeta(body);
	}

}
