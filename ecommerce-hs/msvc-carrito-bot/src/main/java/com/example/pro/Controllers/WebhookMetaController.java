package com.example.pro.Controllers;

import java.io.BufferedReader;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.DTO.metaDTOs.Change;
import com.example.pro.DTO.metaDTOs.Entry;
import com.example.pro.DTO.metaDTOs.MetaWebhookRequest;
import com.example.pro.DTO.metaDTOs.Value;
import com.example.pro.services.IDialogflowService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/webhook")
public class WebhookMetaController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDialogflowService _DialogflowService;
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
    public ResponseEntity<String> receiveWebhook(HttpServletRequest request) {
	try {
	    StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
		buffer.append(line);
	    }
	    String jsonBody = buffer.toString();

	    System.out.println("📥 Received webhook: " + jsonBody);

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode rootNode = mapper.readTree(jsonBody);

	    if (rootNode.has("entry") && rootNode.get("entry").isArray() && rootNode.get("entry").size() > 0) {

		JsonNode entryNode = rootNode.get("entry").get(0);
		if (entryNode.has("changes") && entryNode.get("changes").isArray()
			&& entryNode.get("changes").size() > 0) {

		    JsonNode valueNode = entryNode.get("changes").get(0).get("value");

		    if (valueNode.has("messages") && valueNode.get("messages").isArray()
			    && valueNode.get("messages").size() > 0) {

			JsonNode msgNode = valueNode.get("messages").get(0);
			String telefono = msgNode.get("from").asText();
			String mensaje = msgNode.has("text") ? msgNode.get("text").get("body").asText() : "";

			System.out.println("📱 De: " + telefono);
			System.out.println("💬 Mensaje: " + mensaje);

			if (valueNode.has("contacts") && valueNode.get("contacts").isArray()
				&& valueNode.get("contacts").size() > 0) {

			    JsonNode contactNode = valueNode.get("contacts").get(0);
			    String nombre = contactNode.has("profile") ? contactNode.get("profile").get("name").asText()
				    : "Desconocido";

			    System.out.println("👤 Nombre: " + nombre);
			    _DialogflowService.sendDialogFlow(telefono,nombre,mensaje);
			}
		    }
		}
	    }

	    return ResponseEntity.ok("EVENT_RECEIVED");
	} catch (Exception e) {
	    System.err.println("❌ Error en webhook: " + e.getMessage());
	    e.printStackTrace();
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
	}
    }

}
