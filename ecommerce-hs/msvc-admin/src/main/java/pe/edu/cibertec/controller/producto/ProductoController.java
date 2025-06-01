package pe.edu.cibertec.controller.producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.dto.producto.ProductoDTO;
import pe.edu.cibertec.feign.ProductoClient;
import pe.edu.cibertec.model.producto.Producto;

@Controller
@RequestMapping("/mantenimiento")
public class ProductoController {

    @Autowired
    ProductoClient _productoClient;

    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String categoria,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model){
        PageDTO<ProductoDTO> productosPage = _productoClient.getProductos(nombre, categoria, page);

        model.addAttribute("productosPage", productosPage);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        model.addAttribute("page", page);
        return "listado";
    }
    @GetMapping("/producto/{id}")
    public String getProductoById(@PathVariable Integer id, Model model) {
        model.addAttribute("productTosave", _productoClient.getProductoById(id));
        return "productTosave";
    }

    /*@GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model){
        model.addAttribute("producto", new Producto());
        return "admin/productos/formulario";
    }*/

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO producto, RedirectAttributes flash) {
        if(_productoClient.crearProducto(producto)!=null)
            flash.addFlashAttribute("success",String.format(" El producto %s con ID: %d, se guardó con éxito", producto.descripcion(), producto.IdProducto()));
        else
            flash.addFlashAttribute("error", "error al guardar producto");
        return "redirect:/mantenimiento/productos";
    }

    /*@GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        ProductoDTO producto = _productoClient.getProductoById(id);
        model.addAttribute("producto", producto);
        return "admin/productos/formulario";
    }*/

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @ModelAttribute ProductoDTO producto,RedirectAttributes flash) {
        if(_productoClient.actualizarProducto(id, producto)!=null){
            flash.addFlashAttribute("success",String.format(" El producto %s con ID: %d, se actualizó con éxito", producto.descripcion(), producto.IdProducto()));
        }
        else
            flash.addFlashAttribute("error", "error al actualizar producto");
        return "redirect:/mantenimiento/productos";
    }

}
