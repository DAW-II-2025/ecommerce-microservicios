package pe.edu.cibertec.service.impl;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.client.IAdminClient;
import pe.edu.cibertec.model.entity.Message;

@Service
@RequiredArgsConstructor
public class MessageNotificationConsumer {
	private final IAdminClient _AdminClient;
//    @KafkaListener(topics = "message", groupId = "notificacion-group")
    public void listen(Message mensaje) {
        try {
        	ResponseEntity<Map<String, Object>> response = _AdminClient.publish(mensaje);
            System.out.println("Message received for Consumer: " + mensaje.toString());            
        } catch (Exception e) {
            System.err.println("Error deserializing message: " + e.getMessage());
        }
    }
}
