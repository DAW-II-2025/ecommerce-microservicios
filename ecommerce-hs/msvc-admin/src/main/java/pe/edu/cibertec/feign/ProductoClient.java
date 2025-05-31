package pe.edu.cibertec.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.model.producto.Producto;

@FeignClient(name = "producto-service", url = "http://localhost:8080")
public interface ProductoClient {
    @GetMapping("/Producto/list")
    PageDTO<Producto> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page
    );
    @GetMapping("Producto/{id}")
    Producto getProductoById(@PathVariable Long id);

    @PostMapping("/Producto/create")
    Producto crearProducto(@RequestBody Producto producto);

    @PutMapping("/Producto/update/{id}")
    Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto);
}
