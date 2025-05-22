package com.example.pro.services;

import java.util.List;
import java.util.Optional;

import com.example.pro.model.Cliente;

public interface IClienteServices {
    List<Cliente> GetAllClientes();
    Cliente SaveCliente(Cliente entity);
    Integer updateCliente(Integer id, Cliente cliente);
    Cliente FindClienteById(int id);
    Cliente VerificarCliente(String correo, String Pass);
    Cliente getByDni(String dni);
    Optional<Cliente> verificarCorreo(String correo);
}
