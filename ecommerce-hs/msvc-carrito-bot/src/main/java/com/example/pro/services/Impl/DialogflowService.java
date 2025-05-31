package com.example.pro.services.Impl;

import com.example.pro.DTO.metaDTOs.Text;
import com.example.pro.client.WhatsappClient;
import com.example.pro.model.requestMessage;
import com.example.pro.services.IDialogflowService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.dialogflow.v2.*;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DialogflowService implements IDialogflowService {

    @Autowired
    private WhatsappClient _Client;

    @Value("${META_TOKEN}")
    private String auth;

    private final SessionsClient sessionsClient;

    private final String projectId = "milo-wffb";

    public DialogflowService(SessionsClient sessionsClient) {
	this.sessionsClient = sessionsClient;
    }

    @Override
    public String detectIntent(String text, String languageCode) {
	try {
	    String sessionId = UUID.randomUUID().toString();
	    SessionName session = SessionName.of(projectId, sessionId);

	    TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
	    QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

	    DetectIntentRequest request = DetectIntentRequest.newBuilder().setSession(session.toString())
		    .setQueryInput(queryInput).build();

	    DetectIntentResponse response = sessionsClient.detectIntent(request);
	    QueryResult result = response.getQueryResult();

	    return result.getFulfillmentText();

	} catch (Exception e) {
	    e.printStackTrace();
	    return "Error con Dialogflow";
	}
    }

    @Override
    public void sendDialogFlow(String telefono, String nombre, String mensaje) {
	if (telefono != null && nombre != null && mensaje != null) {
	    String responseDialog = detectIntent(mensaje, "es");
	    if (!responseDialog.equals("Error con Dialogflow")) {
		requestMessage obj = new requestMessage();
		Text text = new Text();
		text.setBody(responseDialog);
		obj.setTo(telefono);
		obj.setMessaging_product("whatsapp");
		obj.setRecipient_type("individual");
		obj.setType("text");
		obj.setText(text);
		try {
		    log.info("enviendo respuesta whatsapp mediante api...");
		    _Client.sendMesagge("Bearer ".concat(auth), obj);
		} catch (FeignException e) {
		    System.err.println("error al enviar mensaje: ".concat(e.getMessage()));
		}
	    }
	}
    }

	@Override
	public ResponseEntity<String> webhookMeta(Map<String, Object> body) {
		try {
//			StringBuilder buffer = new StringBuilder();
//			BufferedReader reader = request.getReader();
//			String line;
//			while ((line = reader.readLine()) != null) {
//				buffer.append(line);
//			}
//			String jsonBody = buffer.toString();
//
//			System.out.println("📥 Received webhook: " + jsonBody);
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.valueToTree(body);

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
							sendDialogFlow(telefono, nombre, mensaje);
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
