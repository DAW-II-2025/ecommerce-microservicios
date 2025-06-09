package com.example.pro.services.Impl;

import com.example.pro.client.ArchivosClient;
import com.example.pro.services.IDialogflowService;
import com.example.pro.services.IWhatsappServices;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.dialogflow.v2.*;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DialogflowService implements IDialogflowService {

	private final IWhatsappServices _Client;

	private final SessionsClient sessionsClient;

	private static final String projectId = "milo-wffb";	

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
			// guardando mensaje recibido
			String responseDialog = detectIntent(mensaje, "es");
			if (!responseDialog.equals("Error con Dialogflow")) {
				try {
					log.info("enviendo respuesta whatsapp mediante api...");
					_Client.sendMessage(mensaje, telefono);
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
