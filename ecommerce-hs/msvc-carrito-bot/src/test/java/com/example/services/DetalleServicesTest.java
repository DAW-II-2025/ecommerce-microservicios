package com.example.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pro.Repository.IDetalleRepository;
import com.example.pro.model.Detalle;
import com.example.pro.services.Impl.DetalleServices;

@ExtendWith(MockitoExtension.class)
public class DetalleServicesTest {

    @Mock
    private IDetalleRepository detalleRepository;

    @InjectMocks
    private DetalleServices detalleServices;

    @Test
    public void testGetAllDetalles() {
    	
        Detalle detalle1 = new Detalle();
        Detalle detalle2 = new Detalle();
        
        List<Detalle> listaM = Arrays.asList(detalle1, detalle2);
        when(detalleRepository.findAll()).thenReturn(listaM);

        List<Detalle> resultado = detalleServices.GetAllDetalles();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(detalleRepository, times(1)).findAll();
    }

    @Test
    public void testSaveDetalle() {
    	
        Detalle dt = new Detalle();
        when(detalleRepository.save(dt)).thenReturn(dt);

        Detalle resultado = detalleServices.SaveDetalle(dt);
        assertNotNull(resultado);
        verify(detalleRepository, times(1)).save(dt);
    }

    @Test
    public void testFindDetalleById() {
    	
        Detalle dtid = new Detalle();
        when(detalleRepository.findById(1)).thenReturn(Optional.of(dtid));

        Detalle resultado = detalleServices.FindDetalleById(1);
        assertNotNull(resultado);
        verify(detalleRepository, times(1)).findById(1);
    }

}
