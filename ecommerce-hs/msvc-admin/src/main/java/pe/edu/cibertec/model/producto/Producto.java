package pe.edu.cibertec.model.producto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
//@Table(name = "") le agregan el nombre p
public class Producto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

	private String descripcion;
	private double precioUnidad;
	private int stock;
	private String categoria;
	private String imagen;
	private String estado;

    public Producto(String descripcion, double precioUnidad, int stock, String categoria, String imagen, String estado) {
    }

    public Producto() {

    }
}
