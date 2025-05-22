package com.example.pro.services;


import java.io.IOException;

import com.example.pro.model.Venta;

import net.sf.jasperreports.engine.JRException;

public interface IReporteServices {

    byte[] generarBoleta(Venta ventaSaved) throws JRException, IOException;

}
