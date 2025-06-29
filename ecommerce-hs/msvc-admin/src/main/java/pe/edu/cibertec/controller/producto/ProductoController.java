package pe.edu.cibertec.controller.producto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.cibertec.dto.producto.PageDTO;
import pe.edu.cibertec.dto.producto.ProductoDTO;
import pe.edu.cibertec.feign.ProductoClient;
import pe.edu.cibertec.service.producto.IProductoService;
import pe.edu.cibertec.service.producto.Impl.CloudinaryService;

@Controller
@RequestMapping("/mantenimiento")
public class ProductoController {

    @Autowired
    IProductoService _productoService;
    @Autowired
    ProductoClient _productoClient;
    @Autowired
    CloudinaryService cloudinaryService;
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
    public String guardarProducto(@ModelAttribute ProductoDTO producto,
                                  @RequestParam("archivoImagen") MultipartFile archivoImagen,
                                  RedirectAttributes flash) {
        try {
            // Subir imagen si fue proporcionada
            if (!archivoImagen.isEmpty()) {
                String urlImagen = cloudinaryService.subirImagen(archivoImagen);
                // Establece la URL de imagen en el DTO
                producto = new ProductoDTO(
                        producto.idProducto(),
                        producto.descripcion(),
                        producto.precioUnidad(),
                        producto.stock(),
                        producto.categoria(),
                        urlImagen,
                        producto.estado()
                );
            }
            ResponseEntity<?> response = _productoClient.crearProducto(producto);
            if (response.getStatusCode().is2xxSuccessful()) {
                flash.addFlashAttribute("success", "Producto guardado correctamente");
            } else {
                flash.addFlashAttribute("error", "Error al guardar producto");
            }

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al procesar imagen o guardar producto: " + e.getMessage());
        }

        return "redirect:/mantenimiento/productos";
    }


    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id,
                                     @ModelAttribute ProductoDTO producto,
                                     @RequestParam("archivoImagen") MultipartFile archivoImagen,
                                     RedirectAttributes flash) {
        try {
            String urlImagen = producto.imagen();

            if (archivoImagen != null && !archivoImagen.isEmpty()) {
                urlImagen = cloudinaryService.subirImagen(archivoImagen);
            }

            ProductoDTO productoActualizado = new ProductoDTO(
                    producto.idProducto(),
                    producto.descripcion(),
                    producto.precioUnidad(),
                    producto.stock(),
                    producto.categoria(),
                    urlImagen,
                    producto.estado()
            );

            ResponseEntity<?> response = _productoClient.actualizarProducto(id, productoActualizado);
            if (response.getStatusCode().is2xxSuccessful()) {
                flash.addFlashAttribute("success", String.format("El producto %s se actualizó correctamente", producto.descripcion()));
            } else {
                flash.addFlashAttribute("error", "Error al actualizar producto");
            }

        } catch (Exception e) {
            flash.addFlashAttribute("error", "Error al actualizar producto: " + e.getMessage());
        }

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
            if (archivo == null || archivo.isEmpty()) {
                respuesta.put("mensaje", "No se seleccionó ningún archivo");
                return ResponseEntity.badRequest().body(respuesta);
            }

            String nombreArchivo = archivo.getOriginalFilename();
            if (nombreArchivo == null || (!nombreArchivo.endsWith(".xlsx") && !nombreArchivo.endsWith(".xls"))) {
                respuesta.put("mensaje", "El archivo debe ser de formato Excel (.xlsx o .xls)");
                return ResponseEntity.badRequest().body(respuesta);
            }

            System.out.println("Procesando archivo: " + nombreArchivo);

            _productoService.procesarExcel(archivo);

            respuesta.put("mensaje", "Productos cargados correctamente desde el archivo Excel");
            respuesta.put("fecha", LocalDateTime.now().toString());
            respuesta.put("success", true);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            System.err.println("Error al procesar archivo Excel: " + e.getMessage());
            e.printStackTrace();

            respuesta.put("mensaje", "Error al procesar el archivo: " + e.getMessage());
            respuesta.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

}
