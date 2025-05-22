package com.example.pro.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.model.Producto;
import com.example.pro.services.IProductoServices;

@RestController()
@RequestMapping("/Producto")
public class ProductoRestController {
    @Autowired
    IProductoServices _ProductoServices;

    public ProductoRestController(IProductoServices productoServices) {
	_ProductoServices = productoServices;
    }

    @GetMapping("/list")
    public Page<Producto> getProducts(@RequestParam(required = false) String nombre, 
	    @RequestParam(required = false) String categoria,
	    @RequestParam(defaultValue = "0") int page
	    ) {
	Pageable pageable = PageRequest.of(page, 12);
	return _ProductoServices.GetAllProductos(nombre, categoria,pageable);

    }
}
