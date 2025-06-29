package com.example.services;

import com.example.pro.model.*;
import com.example.pro.services.Impl.ReporteService;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ReporteServiceTest {

    // Instancia real porque no tiene dependencias que debamos simular
    private final ReporteService reporteService = new ReporteService();

    @Test
    public void testGenerarBoleta_Success() throws JRException, IOException {

        Producto producto = new Producto();
        producto.setDescripcion("Mouse");
        producto.setPrecioUnidad(50.0);

        Detalle detalle = new Detalle();
        detalle.setProducto(producto);  // IMPORTANTE para evitar NullPointerException

        Cliente cliente = new Cliente();
        cliente.setNombres("Carlos");
        cliente.setApellidos("Ramirez");
        cliente.setTelefono("987654321");
        cliente.setDni("12345678");

        Venta venta = new Venta();
        venta.setCli(cliente);
        venta.setDetalles(Collections.singletonList(detalle));

        byte[] resultado = reporteService.generarBoleta(venta);
        assertNotNull(resultado);
        assertTrue(resultado.length > 0, "El archivo generado no debe estar vacío");
    }
}
