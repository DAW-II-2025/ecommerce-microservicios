package com.example.pro.services.Impl;

import com.example.pro.services.IDialogflowService;
import com.example.pro.services.IWhatsappServices;

import com.google.cloud.dialogflow.v2.*;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

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
}
