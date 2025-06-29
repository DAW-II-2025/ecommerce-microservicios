package com.example.services;

import com.example.pro.model.Producto;
import com.example.pro.services.Impl.ProductoServices;
import com.example.pro.Repository.IProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServicesTest {

    @Mock
    private IProductoRepository productoRepository;

    @InjectMocks
    private ProductoServices productoServices;

    @Test
    public void testFindProductoByNombre() {
    	
        Producto producto = new Producto();
        producto.setDescripcion("Mouse Gamer");
        when(productoRepository.findByDescripcion("Mouse Gamer")).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoServices.FindProductoByNombre("Mouse Gamer");
        assertTrue(resultado.isPresent());
        assertEquals("Mouse Gamer", resultado.get().getDescripcion());
    }

    @Test
    public void testupdateProducto() {
    	
        Producto mockProducto = new Producto();
        mockProducto.setStock(10);

        Producto actualizado = new Producto();
        actualizado.setStock(15);
        when(productoRepository.findById(1)).thenReturn(Optional.of(mockProducto));
        when(productoRepository.save(any(Producto.class))).thenReturn(actualizado);

        Integer result = productoServices.updateProducto(1, actualizado);
        assertEquals(1, result);
    }

    @Test
    public void testGetAllProductos() {
    	
        Producto p1 = new Producto();
        p1.setDescripcion("Laptop");
        Producto p2 = new Producto();
        p2.setDescripcion("Tablet");

        List<Producto> lista = Arrays.asList(p1, p2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Producto> paged = new PageImpl<>(lista);
        when(productoRepository.findByEstadoNot("D", pageable)).thenReturn(paged);

        Page<Producto> result = productoServices.GetAllProductos(null, null, false, pageable);
        assertEquals(2, result.getContent().size());
        assertEquals("Laptop", result.getContent().get(0).getDescripcion());
    }

    @Test
    public void testgetById() {
    	
        Producto producto = new Producto();
        producto.setDescripcion("Monitor");
        when(productoRepository.findById(2)).thenReturn(Optional.of(producto));

        Producto result = productoServices.getById(2);
        assertEquals("Monitor", result.getDescripcion());
    }

    @Test
    public void testguardarProducto() {
    	
        Producto base = new Producto();
        base.setDescripcion("Teclado");
        base.setPrecioUnidad(120.0);
        base.setCategoria("Accesorios");
        base.setEstado("A");
        base.setStock(20);
        when(productoRepository.findById(1)).thenReturn(Optional.of(new Producto()));
        when(productoRepository.save(any(Producto.class))).thenReturn(base);

        ResponseEntity<?> response = productoServices.guardarProducto(1, base);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testcrearProducto() {
    	
        Producto nuevo = new Producto();
        nuevo.setDescripcion("Impresora");
        when(productoRepository.save(nuevo)).thenReturn(nuevo);

        ResponseEntity<?> response = productoServices.crearProducto(nuevo);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testdesactivarProducto() {
    	
        Producto producto = new Producto();
        producto.setEstado("A");
        when(productoRepository.findById(3)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        ResponseEntity<?> response = productoServices.desactivarProducto(3);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testactivarProducto() {
    	
        Producto producto = new Producto();
        producto.setEstado("D");
        when(productoRepository.findById(4)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);

        ResponseEntity<?> response = productoServices.activarProducto(4);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
