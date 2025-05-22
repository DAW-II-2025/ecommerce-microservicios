package com.example.pro.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.pro.Repository.IProductoRepository;
import com.example.pro.model.Producto;
import com.example.pro.services.IProductoServices;

@Service
public class ProductoServices implements IProductoServices {

    @Autowired
    IProductoRepository _productoRepository;

    public ProductoServices(IProductoRepository productoRepository) {
	_productoRepository = productoRepository;
    }

    @Override
    public Page<Producto> GetAllProductos(String producto, String categoria, Pageable pageable) {
	if ((producto == null || producto.isBlank()) && (categoria == null || categoria.isBlank())) {
	    return _productoRepository.findAll(pageable);
	} else if ((producto == null || producto.isBlank())) {
	    return _productoRepository.findByCategoriaContainingIgnoreCase(categoria, pageable);
	} else if ((categoria == null || categoria.isBlank())) {
	    return _productoRepository.findByDescripcionContainingIgnoreCase(producto, pageable);
	} else {
	    return _productoRepository.findByDescripcionContainingIgnoreCaseAndCategoriaContainingIgnoreCase(producto,
		    categoria, pageable);
	}
    }

    @Override
    public Optional<Producto> FindProductoByNombre(String nombre) {
	return _productoRepository.findByDescripcion(nombre);
    }

    @Override
    public Integer updateProducto(Integer id, Producto producto) {
	Optional<Producto> existingProducto = _productoRepository.findById(id);
	if (existingProducto.isPresent()) {
	    Producto ProductoToUpdate = existingProducto.get();
	    ProductoToUpdate.setStock(producto.getStock());
	    _productoRepository.save(ProductoToUpdate);
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public Producto getById(int producto) {
	return _productoRepository.findById(producto).get();
    }
}
