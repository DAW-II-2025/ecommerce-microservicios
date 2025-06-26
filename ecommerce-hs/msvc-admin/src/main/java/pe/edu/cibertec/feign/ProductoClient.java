package pe.edu.cibertec.feign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.dto.producto.ProductoDTO;

@FeignClient(name = "producto-service", url = "${msvc.carrito.bot}")
public interface ProductoClient {
    @GetMapping("/Producto/list")
    PageDTO<ProductoDTO> getProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "false") boolean mostrarInactivos
    );

    @GetMapping("Producto/{id}")
    ProductoDTO getProductoById(@PathVariable Integer id);

    @PostMapping("/Producto/guardar")
    ResponseEntity<?> crearProducto(@RequestBody ProductoDTO producto);

    @PutMapping("/Producto/actualizar/{id}")
    ResponseEntity<?> actualizarProducto(@PathVariable Integer id, @RequestBody ProductoDTO producto);

    @PutMapping("/Producto/desactivar/{id}")
    ResponseEntity<?> desactivarProducto(@PathVariable Integer id);

    @PutMapping("/Producto/activar/{id}")
    ResponseEntity<?> activarProducto(@PathVariable Integer id);

}
