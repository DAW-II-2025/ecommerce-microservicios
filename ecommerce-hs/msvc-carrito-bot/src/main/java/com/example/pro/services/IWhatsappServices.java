package com.example.pro.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public interface IWhatsappServices {
    void sendMessage(String msg, String num);
    void sendImage(byte[] img, String num);
	ResponseEntity<String> webhookMeta(HttpServletRequest body);
}
