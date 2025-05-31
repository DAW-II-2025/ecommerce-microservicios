package com.example.pro.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.model.Producto;
import com.example.pro.services.IProductoServices;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
			@RequestParam(required = false) String categoria, @RequestParam(defaultValue = "0") int page) {
		Pageable pageable = PageRequest.of(page, 12);
		return _ProductoServices.GetAllProductos(nombre, categoria, pageable);

	}

	@PostMapping("/guardar")
	public ResponseEntity<?> guardar(@RequestBody Producto pro) {
		try {
			return _ProductoServices.crearProducto(pro);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error interno: " + e.getMessage()));
		}
	}
	
	@PutMapping("/actualizar/{id}")
	public ResponseEntity<?> actualziar(@PathVariable Integer id, @RequestBody Producto pro) {
		try {
			return _ProductoServices.guardarProducto(pro);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error interno: " + e.getMessage()));
		}
	}

}
