package pe.edu.cibertec.service.chat.Impl;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.dto.chat.ConversationDTO;
import pe.edu.cibertec.model.chat.Message;
import pe.edu.cibertec.service.IMessageService;

@Service
@RequiredArgsConstructor
public class MessageService implements IMessageService {
	private final SimpMessagingTemplate messagingTemplate;
//	@Value("${NUM_BOT}")
//	private String numBot; 
	
	@Override
	public ResponseEntity<?> publicarMensaje(Message mensaje) {
		try {
			System.out.println("Message received: " + mensaje.toString());
            ConversationDTO notificacion = new ConversationDTO(mensaje.getChatId(),
            		mensaje.getSms(), mensaje.getHora());
            messagingTemplate.convertAndSend("/chat/".concat(mensaje.getChatId()), mensaje);
            messagingTemplate.convertAndSend("/noti", notificacion);
			return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "se envió el mensaje",
					"body", mensaje));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "error del servidor: ".concat(e.getMessage())));
		}
	}

}
