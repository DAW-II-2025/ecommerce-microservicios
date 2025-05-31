package com.example.pro.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface IDialogflowService {
    String detectIntent(String text, String languageCode);

    void sendDialogFlow(String telefono, String nombre, String mensaje);

	ResponseEntity<String> webhookMeta(Map<String, Object> body);
}
