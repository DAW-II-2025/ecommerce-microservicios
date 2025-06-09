package pe.edu.cibertec.service;

import org.springframework.http.ResponseEntity;

import pe.edu.cibertec.model.chat.Message;

public interface IMessageService {

	ResponseEntity<?> publicarMensaje(Message message);
	
}
