package com.example.pro.model;

import jakarta.persistence.*;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdCliente;
    private String Apellidos;
    private String Nombres;
    private String Direccion;
    private String FechaNacimiento;
    private char Sexo;
    @Column(unique = true)
    private String correo;
    @Column(unique = true)
    private String dni;
    private String Password;
    private String telefono;
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    private char Estado;

    public Cliente(int idCliente, String apellidos, String nombres, String direccion, String fechaNacimiento, char sexo,
	    String correo, String password, char estado) {
	super();
	IdCliente = idCliente;
	Apellidos = apellidos;
	Nombres = nombres;
	Direccion = direccion;
	FechaNacimiento = fechaNacimiento;
	Sexo = sexo;
	this.correo = correo;
	Password = password;
	Estado = estado;
    }

    public Cliente() {
	super();
    }

    public char getEstado() {
	return Estado;
    }

    public void setEstado(char estado) {
	Estado = estado;
    }

    public int getIdCliente() {
	return IdCliente;
    }

    public void setIdCliente(int idCliente) {
	IdCliente = idCliente;
    }

    public String getApellidos() {
	return Apellidos;
    }

    public void setApellidos(String apellidos) {
	Apellidos = apellidos;
    }

    public String getNombres() {
	return Nombres;
    }

    public void setNombres(String nombres) {
	Nombres = nombres;
    }

    public String getDireccion() {
	return Direccion;
    }

    public void setDireccion(String direccion) {
	Direccion = direccion;
    }

    public String getFechaNacimiento() {
	return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
	FechaNacimiento = fechaNacimiento;
    }

    public char getSexo() {
	return Sexo;
    }

    public void setSexo(char sexo) {
	Sexo = sexo;
    }

    public String getCorreo() {
	return correo;
    }

    public void setCorreo(String correo) {
	this.correo = correo;
    }

    public String getPassword() {
	return Password;
    }

    public void setPassword(String password) {
	Password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

}
