package com.example.pro.services;

import java.io.IOException;

import com.mercadopago.resources.Payment;

import net.sf.jasperreports.engine.JRException;

public interface IPaymentService {

    void generarVentaConMercadoPago(Payment Mpago)  throws IOException,JRException ;

    void cancelarVenta(Payment payment);
}
