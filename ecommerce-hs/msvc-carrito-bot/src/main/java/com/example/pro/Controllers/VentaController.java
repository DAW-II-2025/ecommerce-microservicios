package com.example.pro.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.model.Venta;
import com.example.pro.services.IVentaServices;

@RestController
@RequestMapping("/Venta")
public class VentaController {

	@Autowired
	IVentaServices ventaServices;
	public VentaController(IVentaServices ventaServices) {
		this.ventaServices=ventaServices;
	}
	@GetMapping("/mis-ventas")
	public List<Venta> getMethodName() {
		return ventaServices.misVentas();
	}
	

	@GetMapping("/list")
	public List<Venta> getAll() {
		return ventaServices.GetAllVentas();
	}

	@GetMapping("/{id}")
	public Venta getAll(@PathVariable int id) {
		return ventaServices.FindVentaById(id);
	}

	@PostMapping("/add")
	public Venta saveVenta(@RequestBody VentaAndDetalles entity) {
		return ventaServices.SaveVentaAndDetalles(entity);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Integer> deleteVenta(@PathVariable Integer id) {
		Integer deleted = ventaServices.deleteVenta(id);
		if (deleted == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
