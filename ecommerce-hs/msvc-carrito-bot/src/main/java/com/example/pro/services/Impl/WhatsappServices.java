package com.example.pro.services.Impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pro.DTO.MessageArchivos;
import com.example.pro.DTO.metaDTOs.ImageMessage;
import com.example.pro.DTO.metaDTOs.Text;
import com.example.pro.client.ArchivosClient;
import com.example.pro.client.WhatsappClient;
import com.example.pro.client.WhatsappMultipartClient;
import com.example.pro.model.requestMessage;
import com.example.pro.services.IDialogflowService;
import com.example.pro.services.IWhatsappServices;
import com.example.pro.utils.ByteArrayMultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WhatsappServices implements IWhatsappServices {
	private Logger log = LoggerFactory.getLogger(WhatsappServices.class);

	private final WhatsappClient client;

	private final WhatsappMultipartClient clientMultipart;

	private final ArchivosClient archivosClient;

	private final IDialogflowService _DialogflowService;

	@Value("${META_TOKEN}")
	private String auth;

	@Override
	public void sendMessage(String msg, String num) {
		Text text = new Text();
		text.setBody(msg);
		String telefono = "51".concat(num.contains("+51") ? num.replace("+51", "") : num);
		requestMessage requestbody = new requestMessage(telefono, "text", null, text);
		ResponseEntity<Map<String, Object>> response = client.sendMesagge("Bearer ".concat(auth), requestbody);
		if (response.getStatusCode().equals(HttpStatus.OK))
			archivosClient.guardarMensaje(new MessageArchivos(msg, telefono));
	}

	@Override
	public void sendImage(byte[] img, String num) {
		log.info("Tamaño del byte[] generado: {}", img.length);
		String telefono = "51".concat(num.contains("+51") ? num.replace("+51", "") : num);
		String id = null;
		try {

			MultipartFile multipartFile = new ByteArrayMultipartFile(img, "file", "comprobante.jpg", "image/jpeg");
			log.info("Subiendo archivo a WhatsApp con tamaño {} bytes", img.length);
			id = clientMultipart.uploadMedia(multipartFile, "whatsapp", "Bearer " + auth).get("id").toString();
			log.info("Imagen subida con éxito, ID: {}", id);
		} catch (FeignException e) {
			log.error("Error al subir imagen: {}", e.getMessage());
			log.error("Respuesta completa: {}", e.contentUTF8());
		}

		ImageMessage imgMsm = new ImageMessage();
		imgMsm.setId(id);
		requestMessage requestbody2 = new requestMessage(telefono, "image", imgMsm, null);
		client.sendMesagge("Bearer ".concat(auth), requestbody2);
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
							_DialogflowService.sendDialogFlow(telefono, nombre, mensaje);
							archivosClient.guardarMensaje(new MessageArchivos(mensaje, telefono));

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
