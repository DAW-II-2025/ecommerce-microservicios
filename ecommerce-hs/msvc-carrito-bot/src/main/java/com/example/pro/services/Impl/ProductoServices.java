package com.example.pro.services.Impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Override
	public ResponseEntity<?> guardarProducto(Producto pro) {

		Optional<Producto> productoExistente = _productoRepository.findById(pro.getIdProducto());
		if (productoExistente.isPresent()) {
			Producto p = productoExistente.get();
			p.setDescripcion(pro.getDescripcion());
			p.setPrecioUnidad(pro.getPrecioUnidad());
			p.setCategoria(pro.getCategoria());
			p.setEstado(pro.getEstado());
			p.setImagen(pro.getImagen());
			p.setStock(pro.getStock());
			Producto actualizado = _productoRepository.save(p);
			return ResponseEntity.status(HttpStatus.OK).body(actualizado);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Producto no encontrado"));
		}

	}

	@Override
	public ResponseEntity<?> crearProducto(Producto pro) {
		Producto nuevo = _productoRepository.save(pro);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
	}

	@Override
	public ResponseEntity<?> desactivarProducto(Integer id) {
		Optional<Producto> optional = _productoRepository.findById(id);
		if (optional.isPresent()) {
			Producto pro = optional.orElseThrow();
			pro.setEstado("D");
			return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Producto desactivado"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Producto no encontrado"));
	}

	@Override
	public ResponseEntity<?> activarProducto(Integer id) {
		Optional<Producto> optional = _productoRepository.findById(id);
		if (optional.isPresent()) {
			Producto pro = optional.orElseThrow();
			pro.setEstado("A");
			return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Producto activado"));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Producto no encontrado"));	
	}

}
