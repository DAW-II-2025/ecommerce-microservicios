package pe.edu.cibertec.controller.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.feign.ProductoClient;
import pe.edu.cibertec.model.producto.Producto;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    @Autowired
    ProductoClient _productoClient;

    public String listarProductos(@RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String categoria,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model){
        PageDTO<Producto> productosPage = _productoClient.getProductos(nombre, categoria, page);

        model.addAttribute("productosPage", productosPage);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        model.addAttribute("page", page);
        return "admin/productos/listar";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model){
        model.addAttribute("producto", new Producto());
        return "admin/productos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        _productoClient.crearProducto(producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = _productoClient.getProductoById(id);
        model.addAttribute("producto", producto);
        return "admin/productos/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute Producto producto) {
        _productoClient.actualizarProducto(id, producto);
        return "redirect:/admin/productos";
    }

}
