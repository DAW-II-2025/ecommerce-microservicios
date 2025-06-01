package pe.edu.cibertec.controller.chat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import pe.edu.cibertec.model.chat.Message;

@Controller
public class MessageController {
	
	@MessageMapping("/message")
	@SendTo("/chat/message")
	public Message recibeMessage(Message message) {
		message.setFecha(LocalDate.now().toString());
		message.setHora(LocalTime.now().toString());
		return message;
	}
}
