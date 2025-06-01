package pe.edu.cibertec.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.dto.producto.ProductoDTO;
import pe.edu.cibertec.model.producto.Producto;

@FeignClient(name = "producto-service", url = "http://localhost:8080")
public interface ProductoClient {
    @GetMapping("/Producto/list")
    PageDTO<ProductoDTO> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page
    );
    @GetMapping("Producto/{id}")
    ProductoDTO getProductoById(@PathVariable Integer id);

    @PostMapping("/Producto/create")
    ProductoDTO crearProducto(@RequestBody ProductoDTO producto);

    @PutMapping("/Producto/update/{id}")
    ProductoDTO actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDTO producto);

    @PutMapping("/Producto/desactivar/{id}")
    String desactivarProducto(@PathVariable Integer id);
}
