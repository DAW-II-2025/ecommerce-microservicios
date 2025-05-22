package com.example.pro.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.pro.model.requestMessage;

@FeignClient(name = "whatsapp", url = "https://graph.facebook.com/v22.0/${IDENT_CUENTA_META}")
public interface WhatsappClient {

    @PostMapping("/messages")
    ResponseEntity<Map<String, Object>> sendMesagge(@RequestHeader(name = "Authorization") String auth,
	    @RequestBody requestMessage msg);

}
