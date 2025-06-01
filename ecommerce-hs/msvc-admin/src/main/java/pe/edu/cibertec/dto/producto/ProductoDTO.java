package pe.edu.cibertec.dto.producto;



public record ProductoDTO(
         Integer IdProducto,
         String descripcion,
         double PrecioUnidad,
         int Stock,
         String categoria,
         String Imagen,
         String Estado
) {
}
