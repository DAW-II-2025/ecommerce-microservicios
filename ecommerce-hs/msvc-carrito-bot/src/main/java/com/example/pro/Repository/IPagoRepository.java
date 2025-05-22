package com.example.pro.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.pro.model.Pago;

@Repository
public interface IPagoRepository extends CrudRepository<Pago, String> {

}
