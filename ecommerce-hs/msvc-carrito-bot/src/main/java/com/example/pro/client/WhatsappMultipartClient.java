package com.example.pro.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.pro.FeignSupportConfig;

@FeignClient(name = "whatsapp-multipart", url = "https://graph.facebook.com/v22.0/${IDENT_CUENTA_META}", 
configuration = FeignSupportConfig.class)
public interface WhatsappMultipartClient {
    @PostMapping(value = "/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadMedia(@RequestPart("file") MultipartFile file,
	    @RequestPart("messaging_product") String messagingProduct,
	    @RequestHeader("Authorization") String authorization);
}
