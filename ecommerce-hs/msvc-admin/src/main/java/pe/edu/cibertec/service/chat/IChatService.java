package pe.edu.cibertec.service.chat;

import java.util.List;

import pe.edu.cibertec.dto.chat.Conversation;
import pe.edu.cibertec.dto.chat.ConversationDTO;

public interface IChatService {
	
	List<ConversationDTO> findConversation();

	Conversation findConversationById(String chatId);

}
