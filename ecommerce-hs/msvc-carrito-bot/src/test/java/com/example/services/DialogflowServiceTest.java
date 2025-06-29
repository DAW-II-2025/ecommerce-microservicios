package com.example.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.pro.services.Impl.DialogflowService;
import com.google.cloud.dialogflow.v2.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
public class DialogflowServiceTest {

    @Mock
    private SessionsClient sessionsClient;

    @InjectMocks
    private DialogflowService dialogflowService;

    @Test
    public void testdetectIntent_Success() {

        String inputText = "Hola";
        String expectedResponse = "Hola ¿como estas?";

        QueryResult mockQueryResult = QueryResult.newBuilder().setFulfillmentText(expectedResponse).build();

        DetectIntentResponse mockResponse = DetectIntentResponse.newBuilder().setQueryResult(mockQueryResult).build();
        when(sessionsClient.detectIntent(any(DetectIntentRequest.class))).thenReturn(mockResponse);


        String actualResponse = dialogflowService.detectIntent(inputText, "es");
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testsendDialogFlow_Success() {

        String telefono = "987654321";
        String nombre = "Juan";
        String mensaje = "Hola";
        String respuestaDialogflow = "Hola Juan! ¿En que puedo ayudarte?";


        DialogflowService spyService = Mockito.spy(dialogflowService);
        doReturn(respuestaDialogflow).when(spyService).detectIntent(mensaje, "es");

        String result = spyService.sendDialogFlow(telefono, nombre, mensaje);
        assertNotNull(result);
        assertEquals(respuestaDialogflow, result);
    }
}