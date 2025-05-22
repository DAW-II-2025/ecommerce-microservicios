package com.example.pro.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.model.Cliente;
import com.example.pro.services.IClienteServices;


@RestController
@RequestMapping("/Cliente")
public class ClienteRestController {
	@Autowired
	IClienteServices clienteServices;
	public ClienteRestController(IClienteServices clienteservices) {
		clienteServices = clienteservices;
	}
	@GetMapping("/list")
	public List<Cliente> getListCli() {
		return clienteServices.GetAllClientes();
	}

	@GetMapping("/{id}")
	public Cliente getCli(@PathVariable int id) {
		return clienteServices.FindClienteById(id);
	}
	@PostMapping("/add")
	public ResponseEntity<?> postAddCli(@RequestBody Cliente entity) {
	    try {		
		return ResponseEntity.ok(clienteServices.SaveCliente(entity));
	    }catch (DataIntegrityViolationException e) {
		e.printStackTrace();
		return ResponseEntity.internalServerError().body(Map.of("error","ya exíste el usuario "+ entity.getCorreo()));
	    }
	    catch (Exception e) {
		e.printStackTrace();
		return ResponseEntity.internalServerError().body("error del servidor");
	    }
	}
	
	@PutMapping("/{id}")
	public int putMethodName(@PathVariable int id, @RequestBody Cliente entity) {
		return clienteServices.updateCliente(id, entity);
	}
	@PostMapping("/verificar-correo")
	public ResponseEntity<?> verificarCorreo(@RequestParam String correo) {	
	     Optional<Cliente> cliOp = clienteServices.verificarCorreo(correo);
	      return cliOp.map(cli ->{
		return  ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","ya existe el usuario con ese correo"));
	     }).orElse(ResponseEntity.ok(Map.of("message","verificado")));
	     
	}
	@PostMapping("/verificar")
	public Cliente verificar(@RequestBody Map<String, String> request) {
		String correo = request.get("correo");
		String pass = request.get("pass");
		return clienteServices.VerificarCliente(correo, pass);
	}

}
