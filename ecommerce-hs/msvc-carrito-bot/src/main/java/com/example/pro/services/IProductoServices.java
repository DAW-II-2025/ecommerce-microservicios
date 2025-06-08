package com.example.pro.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.example.pro.model.Producto;

public interface IProductoServices {
   
    Optional<Producto> FindProductoByNombre(String nombre);
    Integer updateProducto(Integer id, Producto producto);
    Page<Producto> GetAllProductos(String nombre, String categoria, boolean mostrarInactivos, Pageable pageable);
     Producto getById(int producto);
	ResponseEntity<?> guardarProducto(Integer id, Producto pro);
	ResponseEntity<?> crearProducto(Producto pro);
	ResponseEntity<?> desactivarProducto(Integer id);
	ResponseEntity<?> activarProducto(Integer id);    
}
