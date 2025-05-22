package com.example.pro.services;

public interface IDialogflowService {
    String detectIntent(String text, String languageCode);

    void sendDialogFlow(String telefono, String nombre, String mensaje);
}
