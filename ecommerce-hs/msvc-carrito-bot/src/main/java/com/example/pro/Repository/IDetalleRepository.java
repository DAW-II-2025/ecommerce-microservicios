package com.example.pro.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pro.model.Detalle;

@Repository
public interface IDetalleRepository extends JpaRepository<Detalle,Integer> {
}
