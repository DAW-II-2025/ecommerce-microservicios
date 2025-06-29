package pe.edu.cibertec.dto.producto;

public record ProductoDTO(
        Integer idProducto,
        String descripcion,
        double precioUnidad,
        int stock,
        String categoria,
        String imagen,
        String estado
) {}
