package com.example.services;

import com.example.pro.DTO.PagoDTO;
import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.DTO.VentaDTO;
import com.example.pro.model.Cliente;
import com.example.pro.model.Pago;
import com.example.pro.model.Pedido;
import com.example.pro.model.Venta;
import com.example.pro.Repository.IPagoRepository;
import com.example.pro.services.Impl.PaymentService;
import com.example.pro.services.IReporteServices;
import com.example.pro.services.IVentaServices;
import com.example.pro.services.IWhatsappServices;
import com.google.gson.Gson;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.datastructures.payment.Payer;
import com.mercadopago.resources.datastructures.payment.Identification;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private IVentaServices ventaServices;

    @Mock
    private IReporteServices reporteServices;

    @Mock
    private IWhatsappServices whatsappServices;

    @Mock
    private IPagoRepository pagoRepository;

    @Mock
    private Gson gson;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void GenerarVentaConMercadoPago() throws IOException, JRException {
 
        Payment payment = mock(Payment.class);
        when(payment.getTransactionAmount()).thenReturn(100.00f);
        when(payment.getPaymentMethodId()).thenReturn("visa");
        when(payment.getStatus()).thenReturn(Payment.Status.approved);
        when(payment.getId()).thenReturn("123456");

        Payer payer = mock(Payer.class);
        Identification identification = mock(Identification.class);
        when(identification.getNumber()).thenReturn("12345678");
        when(payer.getIdentification()).thenReturn(identification);
        when(payment.getPayer()).thenReturn(payer);

        String metadataJson = "{\"ventaDTO\":{\"monto\":0,\"fechaVenta\":\"\"},\"pagoDTO\":{}}";
        when(gson.toJson(any())).thenReturn(metadataJson);

        VentaAndDetalles ventaMock = new VentaAndDetalles();
        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setMonto(100.0);
        ventaMock.setVentaDTO(ventaDTO);
        ventaMock.setPagoDTO(new PagoDTO());
        when(gson.fromJson(eq(metadataJson), eq(VentaAndDetalles.class))).thenReturn(ventaMock);
        when(pagoRepository.findById("123456")).thenReturn(Optional.empty());

        Venta ventaSaved = new Venta();
        Cliente cliente = new Cliente();
        cliente.setTelefono("999999999");
        ventaSaved.setCli(cliente);
        ventaSaved.setMonto(100.0);
        when(ventaServices.SaveVentaAndDetalles(any())).thenReturn(ventaSaved);

        byte[] dummyPdf = new byte[10];
        when(reporteServices.generarBoleta(any())).thenReturn(dummyPdf);
        paymentService.generarVentaConMercadoPago(payment);
        verify(ventaServices, times(1)).SaveVentaAndDetalles(any());
        verify(reporteServices, times(1)).generarBoleta(ventaSaved);
        verify(whatsappServices, times(1)).sendImage(dummyPdf, "999999999");
        verify(whatsappServices, times(1)).sendMessage(contains("💸"), eq("999999999"));
    }
    
    @Test
    public void CancelarVenta() {

        String paymentId = "123456"; 

        Payment payment = mock(Payment.class);
        when(payment.getId()).thenReturn(paymentId);
        when(payment.getStatus()).thenReturn(Payment.Status.cancelled);

        Pago pagoMock = new Pago();
        pagoMock.setId(paymentId);
        when(pagoRepository.findById(paymentId)).thenReturn(Optional.of(pagoMock));

        Venta ventaMock = new Venta();
        ventaMock.setMonto(99.99);
        ventaMock.setPago(pagoMock);

        Cliente cliente = new Cliente();
        cliente.setTelefono("987654321");
        ventaMock.setCli(cliente);

        Pedido pedido = new Pedido();
        ventaMock.setPedido(pedido);
        when(ventaServices.findByPago(pagoMock)).thenReturn(ventaMock);
        when(ventaServices.SaveVenta(any())).thenReturn(ventaMock);
        paymentService.cancelarVenta(payment);
        verify(pagoRepository).findById(paymentId);
        verify(ventaServices).findByPago(pagoMock);
        verify(ventaServices).SaveVenta(any());
        verify(whatsappServices).sendMessage(contains("🚨🚫"), eq("987654321"));
    }
}
