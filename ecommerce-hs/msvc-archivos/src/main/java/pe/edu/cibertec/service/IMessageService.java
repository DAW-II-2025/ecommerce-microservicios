package pe.edu.cibertec.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import pe.edu.cibertec.model.ConversationDTO;
import pe.edu.cibertec.model.entity.Conversation;
import pe.edu.cibertec.model.entity.Message;

public interface IMessageService {

	ResponseEntity<?> guardarMensaje(Message message);

	Conversation findConversationById(String id);

	List<ConversationDTO> findAllConversations();
	
}
