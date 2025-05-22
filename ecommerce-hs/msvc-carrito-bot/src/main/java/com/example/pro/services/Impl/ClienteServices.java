package com.example.pro.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pro.Repository.IClienteRepository;
import com.example.pro.model.Cliente;
import com.example.pro.services.IClienteServices;

@Service
public class ClienteServices implements IClienteServices {

    @Autowired
    private IClienteRepository _clienteRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<Cliente> GetAllClientes() {
	return _clienteRepository.findAll();
    }

    @Override
    public Cliente SaveCliente(Cliente entity) {
	entity.setPassword(encoder.encode(entity.getPassword()));
	Cliente clienteSaved = _clienteRepository.save(entity);
	return clienteSaved;
    }

    @Override
    public Integer updateCliente(Integer id, Cliente cliente) {
	Optional<Cliente> existingCliente = _clienteRepository.findById(id);
	if (existingCliente.isPresent()) {
	    Cliente ClienteToUpdate = existingCliente.get();
	    ClienteToUpdate.setApellidos(cliente.getApellidos());
	    ClienteToUpdate.setNombres(cliente.getNombres());
	    ClienteToUpdate.setDireccion(cliente.getDireccion());
	    ClienteToUpdate.setFechaNacimiento(cliente.getFechaNacimiento());
	    ClienteToUpdate.setNombres(cliente.getNombres());
	    ClienteToUpdate.setSexo(cliente.getSexo());
	    ClienteToUpdate.setCorreo(cliente.getCorreo());
	    ClienteToUpdate.setPassword(encoder.encode(cliente.getPassword()));
	    ClienteToUpdate.setEstado(cliente.getEstado());
	    _clienteRepository.save(ClienteToUpdate);
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public Cliente FindClienteById(int id) {
	Optional<Cliente> rowInDB = _clienteRepository.findById(id);
	if (rowInDB.isPresent()) {
	    return rowInDB.get();
	} else {
	    return new Cliente();
	}
    }

    @Override
    public Cliente VerificarCliente(String correo, String Pass) {
	Optional<Cliente> Cli = _clienteRepository.findbyCorreo(correo);
	if (Cli.isPresent()) {
	    Cliente cliente = Cli.get();
	    if (cliente.getPassword().equals(Pass))
		return cliente;
	}
	return null;
    }

    @Override
    public Cliente getByDni(String dni) {
	return _clienteRepository.findByDni(dni).orElseThrow();
    }

    @Override
    public Optional<Cliente> verificarCorreo(String correo) {
	return _clienteRepository.findbyCorreo(correo);
    }
}
