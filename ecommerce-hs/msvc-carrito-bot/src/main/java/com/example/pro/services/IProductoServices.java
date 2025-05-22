package com.example.pro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.pro.model.Producto;

public interface IProductoServices {
   
    Optional<Producto> FindProductoByNombre(String nombre);
    Integer updateProducto(Integer id, Producto producto);
    Page<Producto> GetAllProductos(String nombre, String categoria, Pageable pageable);
     Producto getById(int producto);    
}
