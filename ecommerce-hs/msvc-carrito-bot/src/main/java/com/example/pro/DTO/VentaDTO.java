package com.example.pro.DTO;

public class VentaDTO {
    private String cli;
    private String FechaVenta;
    private double monto;
    public VentaDTO() {
	super();
    }

    public VentaDTO(String cli, String fechaVenta, double monto) {
	super();
	this.cli = cli;
	FechaVenta = fechaVenta;
	this.monto = monto;
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

    public String getCli() {
	return cli;
    }

    public void setCli(String cli) {
	this.cli = cli;
    }    

}
