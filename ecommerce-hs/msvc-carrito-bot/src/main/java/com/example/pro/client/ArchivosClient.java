package com.example.pro.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.pro.DTO.MessageArchivos;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "msvc-archivos", url = "localhost:8083")
public interface ArchivosClient {

	@PostMapping
	ResponseEntity<?> guardarMensaje(@RequestBody MessageArchivos message); 
}
