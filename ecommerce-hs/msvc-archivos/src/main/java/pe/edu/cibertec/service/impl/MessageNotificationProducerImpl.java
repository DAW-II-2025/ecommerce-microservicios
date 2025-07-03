package pe.edu.cibertec.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.model.entity.Message;
import pe.edu.cibertec.service.IMessageNotificationService;

@Service
@RequiredArgsConstructor
public class MessageNotificationProducerImpl implements IMessageNotificationService {

	private final KafkaTemplate<String, String> kafkatemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void envioNotificacion(Message mensaje) {
		try {
			String json = objectMapper.writeValueAsString(mensaje);
			kafkatemplate.send("message", json);
			System.out.println("Mensaje enviado desde el PRODUCER MessageNotificationProducerImpl: " + mensaje);
		} catch (JsonProcessingException
				e) {
			e.printStackTrace();
		}
	}
}