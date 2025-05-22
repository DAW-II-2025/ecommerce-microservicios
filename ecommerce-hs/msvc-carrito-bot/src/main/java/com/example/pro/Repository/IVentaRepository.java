package com.example.pro.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pro.model.Cliente;
import com.example.pro.model.Pago;
import com.example.pro.model.Venta;

@Repository
public interface IVentaRepository extends JpaRepository <Venta,Integer>{

    List<Venta> findByCli(Cliente usuarioActual);

    Optional<Venta> findByPago(Pago pago);
}
