package pe.edu.cibertec.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.model.entity.Conversation;
import pe.edu.cibertec.model.entity.Message;
import pe.edu.cibertec.repository.IConverationRepository;
import pe.edu.cibertec.repository.IMessageRepository;
import pe.edu.cibertec.service.IMessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService{
	
//	private final IMessageRepository _IMessageRepository;
	private final IConverationRepository _ConverationRepository;
	@Override
	public ResponseEntity<?> guardarMensaje(Message message) {
		message.setFecha(LocalDate.now().toString());
		message.setHora(LocalTime.now().toString());
		String id = "num-bot-".concat(message.getSender());
		Optional<Conversation> op = _ConverationRepository.findById(id);
		if (op.isPresent()) {			
			Conversation conversation = op.get();
			conversation.getMessages().add(message);
			_ConverationRepository.save(conversation);
			return ResponseEntity.status(HttpStatus.OK).body("se guardó el mensaje nuevo");
		}		
		Conversation conver = new Conversation();
		conver.setChatId(id);
		conver.setMessages(new ArrayList<>());
		conver.getMessages().add(message);
		_ConverationRepository.save(conver);
		return ResponseEntity.status(HttpStatus.CREATED).body("se creó el mensaje nuevo y la conversacion nueva");
	}
	@Override
	public Conversation findConversationById(String id) {
		return _ConverationRepository.findById(id).orElse(new Conversation());
	}

}
