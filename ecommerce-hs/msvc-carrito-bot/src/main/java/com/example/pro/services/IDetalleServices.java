package com.example.pro.services;

import java.util.List;

import com.example.pro.model.Detalle;

public interface IDetalleServices {
    List<Detalle> GetAllDetalles();
    Detalle SaveDetalle(Detalle entity);
    Detalle FindDetalleById(int id);
}
