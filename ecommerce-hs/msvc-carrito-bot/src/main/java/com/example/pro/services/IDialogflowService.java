package com.example.pro.services;

public interface IDialogflowService {
    String detectIntent(String text, String languageCode);

    String sendDialogFlow(String telefono, String nombre, String mensaje);
	
}
