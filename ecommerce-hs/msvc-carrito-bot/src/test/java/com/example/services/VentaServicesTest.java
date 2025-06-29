package com.example.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.pro.DTO.*;
import com.example.pro.Repository.IClienteRepository;
import com.example.pro.Repository.IDetalleRepository;
import com.example.pro.Repository.IProductoRepository;
import com.example.pro.Repository.IVentaRepository;
import com.example.pro.model.*;
import com.example.pro.services.IUsuarioServices;
import com.example.pro.services.Impl.ProductoServices;
import com.example.pro.services.Impl.VentaServices;
import com.example.pro.model.Venta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VentaServicesTest {
    @Mock
    private IVentaRepository ventaRepository;
    @Mock
    private IProductoRepository productoRepository;
    @Mock
    private IDetalleRepository detalleRepository;
    @Mock
    private IClienteRepository clienteRepository;
    @Mock
    private IUsuarioServices UsuarioServices;
    @Mock
    private ProductoServices productoServices;

    @InjectMocks
    private VentaServices ventaServices;

    @Test
    public void testGetAllVentas() {

        Venta venta1 = new Venta();
        Venta venta2 = new Venta();
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(venta1, venta2));

        List<Venta> resultado = ventaServices.GetAllVentas();
        assertEquals(2, resultado.size());
        verify(ventaRepository, times(1)).findAll();
    }
    
    @Test
    public void testSaveVenta() {

        Venta venta = new Venta();
        venta.setMonto(150.0);
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaServices.SaveVenta(venta);
        assertNotNull(resultado);
        assertEquals(150.0, resultado.getMonto());
        verify(ventaRepository, times(1)).save(venta);
    }
    
    @Test
    public void testFindVentaById() {

        Venta venta = new Venta();
        venta.setIdVenta(1);
        venta.setMonto(200.0);
        when(ventaRepository.findById(1)).thenReturn(Optional.of(venta));

        Venta resultado = ventaServices.FindVentaById(1);
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdVenta());
        assertEquals(200.0, resultado.getMonto());
        verify(ventaRepository, times(1)).findById(1);
    }
    
    @Test
    public void testDeleteVenta() {

        Venta venta = new Venta();
        venta.setIdVenta(5);
        when(ventaRepository.findById(5)).thenReturn(Optional.of(venta));

        Integer resultado = ventaServices.deleteVenta(5);
        assertEquals(1, resultado);
        verify(ventaRepository).deleteById(5);
    }
    
    @Test
    public void testSaveVentaAndDetalles() {

        VentaAndDetalles VAD = new VentaAndDetalles();

        // VentaDTO
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setCli("cliente@gmail.com");
        ventaDTO.setFechaVenta("2025-06-28");
        ventaDTO.setMonto(200.0);
        VAD.setVentaDTO(ventaDTO);

        // PagoDTO
        PagoDTO pagoDTO = new PagoDTO();
        pagoDTO.setPaymentId("P123");
        pagoDTO.setEstado("aprovado");
        pagoDTO.setMetodo("visa");
        VAD.setPagoDTO(pagoDTO);

        // PedidoDTO
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setDistrito("Lima");
        pedidoDTO.setDireccion("Av. Las Casuarinas");
        pedidoDTO.setReferencia("Puerta azul");
        pedidoDTO.setNombreReceptor("Juan Castillo");
        pedidoDTO.setTelefono("987654321");
        VAD.setPedidoDTO(pedidoDTO);

        // DetallesDTO
        DetalleDTO detalleDTO = new DetalleDTO();
        detalleDTO.setProducto(1);
        detalleDTO.setCant(2);
        VAD.setDetallesDTO(List.of(detalleDTO));

        // Cliente simulado
        Cliente cliente = new Cliente();
        cliente.setCorreo("cliente@gmail.com");
        when(clienteRepository.findbyCorreo("cliente@gmail.com")).thenReturn(Optional.of(cliente));

        // Producto simulado
        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setStock(10);
        producto.setDescripcion("Mouse");
        producto.setPrecioUnidad(50.0);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // Venta simulada
        Venta ventaGuardada = new Venta();
        ventaGuardada.setIdVenta(99);
        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaGuardada);

        Venta resultado = ventaServices.SaveVentaAndDetalles(VAD);
        assertNotNull(resultado);
        assertEquals(99, resultado.getIdVenta());
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleRepository).save(any(Detalle.class));
        verify(productoServices).updateProducto(eq(1), any(Producto.class));
    }
    
    @Test
    public void testFindByPago() {

        Pago pago = new Pago();
        pago.setId("pago1");

        Venta ventaE = new Venta();	
        ventaE.setIdVenta(1);
        ventaE.setPago(pago);
        when(ventaRepository.findByPago(pago)).thenReturn(Optional.of(ventaE));

        Venta resultado = ventaServices.findByPago(pago);
        assertEquals(ventaE, resultado);
    }
}
