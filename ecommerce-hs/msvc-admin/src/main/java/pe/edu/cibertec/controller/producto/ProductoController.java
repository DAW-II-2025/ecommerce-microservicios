package pe.edu.cibertec.controller.producto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.dto.producto.ProductoDTO;
import pe.edu.cibertec.feign.ProductoClient;
import pe.edu.cibertec.service.producto.IProductoService;

@Controller
@RequestMapping("/mantenimiento")
public class ProductoController {

    @Autowired
    IProductoService _productoService;
    @Autowired
    ProductoClient _productoClient;

    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String categoria,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "false") boolean mostrarInactivos,
                                  Model model) {
        PageDTO<ProductoDTO> productosPage = _productoClient.getProductos(nombre, categoria, page, mostrarInactivos);

        model.addAttribute("productosPage", productosPage);
        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        model.addAttribute("mostrarInactivos", mostrarInactivos);
        model.addAttribute("page", page);
        return "producto/listado";
    }

    @GetMapping("/producto/detalle/{id}")
    public String getProductoById(@PathVariable Integer id, Model model) {
        if (id == 0) {
            ProductoDTO nuevoProducto = new ProductoDTO(0, "", 0.0, 0, "", "", "");
            model.addAttribute("productToSave", nuevoProducto);
            return "producto/productToSave";
        } else {
            model.addAttribute("productToUpdate", _productoClient.getProductoById(id));
            return "producto/productToUpdate";
        }
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model){
        model.addAttribute("producto", new ProductoDTO(null,null,0.0,0,null,null,null));
        return "producto/productToSave";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO producto, RedirectAttributes flash) {
        if(_productoClient.crearProducto(producto)!=null)
            flash.addFlashAttribute("success", String.format(
                    "El producto %s, se guardó con éxito",
                    producto.descripcion()
            ));
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
            flash.addFlashAttribute("success",String.format(" El producto %s con ID: %d, se actualizó con éxito", producto.descripcion(), producto.idProducto()));
        }
        else
            flash.addFlashAttribute("error", "error al actualizar producto");
        return "redirect:/mantenimiento/productos";
    }

    @GetMapping("/desactivar/{id}")
    public String desactivarProducto(@PathVariable Integer id, RedirectAttributes flash) {
        ProductoDTO producto = _productoClient.getProductoById(id);
        if(_productoClient.desactivarProducto(id)!=null) {
            flash.addFlashAttribute("success", String.format(
                    "El producto %s con ID: %d, se eliminó con éxito",
                    producto.descripcion(), producto.idProducto()
            ));
        } else {
            flash.addFlashAttribute("error", "error al eliminar producto");
        }
        return "redirect:/mantenimiento/productos";
    }

    @GetMapping("/activar/{id}")
    public String activarProducto(@PathVariable Integer id, RedirectAttributes flash) {
        ProductoDTO producto = _productoClient.getProductoById(id);
        if(_productoClient.activarProducto(id)!=null) {
            flash.addFlashAttribute("success", String.format(
                    "El producto %s con ID: %d, se activó con éxito",
                    producto.descripcion(), producto.idProducto()
            ));
        } else {
            flash.addFlashAttribute("error", "error al activar producto");
        }
        return "redirect:/mantenimiento/productos";
    }
    
    @PostMapping("/carga-excel")
    public ResponseEntity<Map<String, Object>> cargarExcel(@RequestParam("archivo") MultipartFile archivo) {
        Map<String, Object> respuesta = new HashMap<>();
        try {
        	_productoService.procesarExcel(archivo);
            respuesta.put("mensaje", "Productos cargados correctamente.");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.put("mensaje", "Error al procesar el archivo.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

}
