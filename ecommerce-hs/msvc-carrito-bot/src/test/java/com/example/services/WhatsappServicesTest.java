package com.example.services;

import static org.mockito.Mockito.*;

import com.example.pro.client.ArchivosClient;
import com.example.pro.client.WhatsappClient;
import com.example.pro.client.WhatsappMultipartClient;
import com.example.pro.DTO.MessageArchivos;
import com.example.pro.model.requestMessage;
import com.example.pro.services.IDialogflowService;
import com.example.pro.services.Impl.WhatsappServices;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class WhatsappServicesTest {

    @Mock
    private WhatsappClient whatsappClient;

    @Mock
    private WhatsappMultipartClient whatsappMultipartClient;

    @Mock
    private ArchivosClient archivosClient;

    @Mock
    private IDialogflowService dialogflowService;

    @InjectMocks
    private WhatsappServices whatsappServices;

    @Test
    public void testSendMessage() throws Exception{

        String mensaje = "Hola mundo";
        String numero = "987654321";
        
        // Valor simulado al campo `auth`
        Field authField = WhatsappServices.class.getDeclaredField("auth");
        authField.setAccessible(true);
        authField.set(whatsappServices, "fake_token");

        ResponseEntity<Map<String, Object>> responseMock = new ResponseEntity<>(HttpStatus.OK);
        when(whatsappClient.sendMesagge(anyString(), any(requestMessage.class))).thenReturn(responseMock);
        when(archivosClient.guardarMensaje(any(MessageArchivos.class))).thenReturn(ResponseEntity.ok().build());

        whatsappServices.sendMessage(mensaje, numero);
        verify(whatsappClient, times(1)).sendMesagge(anyString(), any(requestMessage.class));
        verify(archivosClient, times(1)).guardarMensaje(any(MessageArchivos.class));
    }

    @Test
    public void testSendImage() throws Exception{

        byte[] imagen = new byte[10];
        String numero = "987654321";
        
        // Valor simulado al campo `auth`
        Field authField = WhatsappServices.class.getDeclaredField("auth");
        authField.setAccessible(true);
        authField.set(whatsappServices, "fake_token");

        String idMock = "img12";
        when(whatsappMultipartClient.uploadMedia(any(MultipartFile.class), eq("whatsapp"), anyString())).thenReturn(Map.of("id", idMock));
        when(whatsappClient.sendMesagge(anyString(), any(requestMessage.class))).thenReturn(ResponseEntity.ok().build());

        whatsappServices.sendImage(imagen, numero);
        verify(whatsappMultipartClient, times(1)).uploadMedia(any(MultipartFile.class), eq("whatsapp"), anyString());
        verify(whatsappClient, times(1)).sendMesagge(anyString(), any(requestMessage.class));
    }
}
