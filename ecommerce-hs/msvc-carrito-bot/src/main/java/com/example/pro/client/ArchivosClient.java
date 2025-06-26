package com.example.pro.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pro.DTO.MessageArchivos;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "msvc-archivos", url = "${msvc.archivos}")
public interface ArchivosClient {

	@PostMapping("/archivos/message")
	ResponseEntity<String> guardarMensaje(@RequestBody MessageArchivos message); 
}
