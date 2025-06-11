package pe.edu.cibertec.service.chat.Impl;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.dto.chat.MessageArchivos;
import pe.edu.cibertec.model.chat.Message;

@Service
@RequiredArgsConstructor
public class MessageNotificationConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "message", groupId = "notificacion-group")
    public void listen(String mensajeJson) {
        try {
            Message mensaje = objectMapper.readValue(mensajeJson, Message.class);
            System.out.println("Message received: " + mensaje.toString());
            messagingTemplate.convertAndSend("/chat/".concat(mensaje.getChatId()), mensaje);
        } catch (Exception e) {
            System.err.println("Error deserializing message: " + e.getMessage());
        }
    }
}
