package pe.edu.cibertec.service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.model.entity.Message;
import pe.edu.cibertec.service.IMessageNotificationService;

@Service
@RequiredArgsConstructor
public class MessageNotificationServiceImpl implements IMessageNotificationService {

	private final KafkaTemplate<String, Object> kafkatemplate;
	@Override
	
	public void envioNotificacion(Message mensaje) {
		
		kafkatemplate.send("message", mensaje);
		System.out.println("mensaje envidao desde el PRODUCER MessageNotificationServiceImpl: " 
		+ mensaje.toString());
	}
}