package com.example.pro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Detalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdDetalle;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    private int cant;
    
      
    public String getNombre() {
	return producto.getDescripcion();
    };

    public double getPrecioUnidad() {
	return producto.getPrecioUnidad();
    }

    public double getSubTotal() {
	return producto.getPrecioUnidad() * cant;
    }

    public Detalle() {
	super();
    }

    public Detalle(int idDetalle, Venta venta, Producto producto, int cant) {
	super();
	IdDetalle = idDetalle;
	this.venta = venta;
	this.producto = producto;
	this.cant = cant;
    }

}
