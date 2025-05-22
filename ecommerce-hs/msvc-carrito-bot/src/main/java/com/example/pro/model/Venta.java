package com.example.pro.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;

@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdVenta;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cli;
    private String FechaVenta;
    private double monto;
    @OneToOne(cascade = CascadeType.ALL)
    private Pago pago;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Pedido pedido;
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Detalle> detalles;
    private String paymentId;

    @PrePersist
    private void prepersist() {
	paymentId = pago.getId();
    }

    public Pago getPago() {
	return pago;
    }

    public void setPago(Pago pago) {
	this.pago = pago;
    }

    public Pedido getPedido() {
	return pedido;
    }

    public void setPedido(Pedido pedido) {
	this.pedido = pedido;
    }

    public Venta(int idVenta, Cliente cli, String fechaVenta, double monto) {
	super();
	IdVenta = idVenta;
	this.cli = cli;
	FechaVenta = fechaVenta;
	this.monto = monto;
    }

    public Venta() {
	super();
    }

    public String getFechaVenta() {
	return FechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
	FechaVenta = fechaVenta;
    }

    public double getMonto() {
	return monto;
    }

    public void setMonto(double monto) {
	this.monto = monto;
    }

    public int getIdVenta() {
	return IdVenta;
    }

    public void setIdVenta(int idVenta) {
	IdVenta = idVenta;
    }

    public Cliente getCli() {
	return cli;
    }

    public void setCli(Cliente cli) {
	this.cli = cli;
    }

    public String getPaymentId() {
	return paymentId;
    }

    public void setPaymentId(String paymentId) {
	this.paymentId = paymentId;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }
    
    public String getCorrelativo() {
	return String.format("%010d", this.IdVenta);
    }

}
