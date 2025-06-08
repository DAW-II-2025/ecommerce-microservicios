package com.example.pro.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pro.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository <Producto,Integer> {

    List<Producto> findByDescripcionContainingIgnoreCase(String producto); 
    Page<Producto> findByDescripcionContainingIgnoreCase(String producto, Pageable pageable); 
    Page<Producto> findByDescripcionContainingIgnoreCaseAndCategoriaContainingIgnoreCase(String producto,
	    String categoria, Pageable pageable);

    Page<Producto> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
    Page<Producto> findByEstadoNot(String estado, Pageable pageable);
    Page<Producto> findByCategoriaContainingIgnoreCaseAndEstadoNot(String categoria, String estado, Pageable pageable);

    Page<Producto> findByDescripcionContainingIgnoreCaseAndEstadoNot(String descripcion, String estado, Pageable pageable);

    Page<Producto> findByDescripcionContainingIgnoreCaseAndCategoriaContainingIgnoreCaseAndEstadoNot(String descripcion, String categoria, String estado, Pageable pageable);
    Optional<Producto> findByDescripcion(String nombre);

    // Para productos con estado "D"
    Page<Producto> findByEstado(String estado, Pageable pageable);
    Page<Producto> findByCategoriaContainingIgnoreCaseAndEstado(String categoria, String estado, Pageable pageable);
    Page<Producto> findByDescripcionContainingIgnoreCaseAndEstado(String descripcion, String estado, Pageable pageable);
    Page<Producto> findByDescripcionContainingIgnoreCaseAndCategoriaContainingIgnoreCaseAndEstado(String descripcion, String categoria, String estado, Pageable pageable);
}
