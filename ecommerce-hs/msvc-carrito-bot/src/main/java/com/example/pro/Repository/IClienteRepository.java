package com.example.pro.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.pro.model.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Integer> {
    @Query("select p from Cliente p where p.correo = ?1")
    Optional<Cliente> findbyCorreo(String correo);

    Optional<Cliente> findByDni(String dni);
}
