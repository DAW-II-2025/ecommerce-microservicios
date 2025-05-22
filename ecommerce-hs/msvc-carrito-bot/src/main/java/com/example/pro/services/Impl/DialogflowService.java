package com.example.pro.services.Impl;

import com.example.pro.DTO.metaDTOs.Text;
import com.example.pro.client.WhatsappClient;
import com.example.pro.model.requestMessage;
import com.example.pro.services.IDialogflowService;

import com.google.cloud.dialogflow.v2.*;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
