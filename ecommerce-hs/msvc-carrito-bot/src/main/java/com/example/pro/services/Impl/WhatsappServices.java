package com.example.pro.services.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pro.DTO.metaDTOs.Text;
import com.example.pro.client.WhatsappClient;
import com.example.pro.client.WhatsappMultipartClient;
import com.example.pro.model.ImageMessage;
import com.example.pro.model.requestMessage;
import com.example.pro.services.IWhatsappServices;
import com.example.pro.utils.ByteArrayMultipartFile;

import feign.FeignException;

@Service
public class WhatsappServices implements IWhatsappServices {
    private Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private WhatsappClient client;
    @Autowired
    private WhatsappMultipartClient clientMultipart;       

    @Value("${META_TOKEN}")
    private String auth;

    @Override
    public void sendMessage(String msg, String num) {
	Text text = new Text();
	text.setBody(msg);
	String telefono = "51".concat(num.replace("+51", ""));
	requestMessage requestbody = new requestMessage(telefono, "text", null,
		text);
	client.sendMesagge("Bearer ".concat(auth), requestbody);
    }

    @Override
    public void sendImage(byte[] img, String num) {
	log.info("Tamaño del byte[] generado: {}", img.length);
	String telefono = "51".concat(num.replace("+51", ""));
	String id = null;
	try {

	    MultipartFile multipartFile = new ByteArrayMultipartFile(img, "file", "comprobante.jpg",
		    "image/jpeg");
	    log.info("Subiendo archivo a WhatsApp con tamaño {} bytes", img.length);
	    id = clientMultipart.uploadMedia(multipartFile, "whatsapp", "Bearer " + auth).get("id").toString();
	    log.info("Imagen subida con éxito, ID: {}", id);
	} catch (FeignException e) {
	    log.error("Error al subir imagen: {}", e.getMessage());
	    log.error("Respuesta completa: {}", e.contentUTF8());
	}

	ImageMessage imgMsm = new ImageMessage();
	imgMsm.setId(id);
	requestMessage requestbody2 = new requestMessage(telefono, "image", imgMsm,
		null);
	client.sendMesagge("Bearer ".concat(auth), requestbody2);	
    }

}
