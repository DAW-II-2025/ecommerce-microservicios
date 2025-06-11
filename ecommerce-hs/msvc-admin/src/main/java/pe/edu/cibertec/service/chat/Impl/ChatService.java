package pe.edu.cibertec.service.chat.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.dto.chat.Conversation;
import pe.edu.cibertec.dto.chat.ConversationDTO;
import pe.edu.cibertec.feign.ArchivosClient;
import pe.edu.cibertec.service.chat.IChatService;

@Service
@RequiredArgsConstructor
public class ChatService implements IChatService {

	private final ArchivosClient _ArchivosClient;

	@Override
	public List<ConversationDTO> findConversation() {
		try {
			return _ArchivosClient.obtenerConversaciones();
		} catch (FeignException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public Conversation findConversationById(String chatId) {
		try {
			return _ArchivosClient.obtenerConversacion(chatId);
		} catch (FeignException e) {
			e.printStackTrace();
			return new Conversation(chatId, null);
		}	}
}
