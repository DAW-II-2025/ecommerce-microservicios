package pe.edu.cibertec.service.chat.Impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.feign.ArchivosClient;
import pe.edu.cibertec.model.chat.Message;
import pe.edu.cibertec.service.IMessageService;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
	private final ArchivosClient _ArchivosClient;
	private final SimpMessagingTemplate messagingTemplate;
//	@Value("${NUM_BOT}")
//	private String numBot; 

	@Override
	public ResponseEntity<?> publicarMensaje(Message message) {
		try {
			String chatId = message.getSender();
			_ArchivosClient.guardarMensaje(message);
			messagingTemplate.convertAndSend("/chat/".concat(chatId), message);
			return ResponseEntity.status(HttpStatus.OK).body(Map.of(
					"message", ""
					));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
					"error", "error del servidor: ".concat(e.getMessage())
					));
		}
	}

}
