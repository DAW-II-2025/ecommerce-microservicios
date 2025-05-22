package com.example.pro.services;

public interface IWhatsappServices {
    void sendMessage(String msg, String num);
    void sendImage(byte[] img, String num);
}
