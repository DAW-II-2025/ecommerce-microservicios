package pe.edu.cibertec.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.model.ConversationDTO;
import pe.edu.cibertec.model.entity.Conversation;
import pe.edu.cibertec.model.entity.Message;
import pe.edu.cibertec.repository.IConverationRepository;
import pe.edu.cibertec.service.IMessageNotificationService;
import pe.edu.cibertec.service.IMessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService{
	
//	private final IMessageRepository _IMessageRepository;
	private final IConverationRepository _ConverationRepository;
	
	private final IMessageNotificationService _IMessageNotificationService;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private final DateTimeFormatter formattertime = DateTimeFormatter.ofPattern("HH:mm");

	@Override
	public ResponseEntity<?> guardarMensaje(Message message) {
		message.setFecha(LocalDate.now().format(formatter));
		message.setHora(LocalTime.now().format(formattertime));		
		String id = message.getChatId();
		Optional<Conversation> op = _ConverationRepository.findById(id);
		if (op.isPresent()) {			
			Conversation conversation = op.get();
			conversation.getMessages().add(message);
			_ConverationRepository.save(conversation);
			_IMessageNotificationService.envioNotificacion(message);
			return ResponseEntity.status(HttpStatus.OK).body("se guardó el mensaje nuevo");
		}		
		Conversation conver = new Conversation();
		conver.setChatId(id);
		conver.setMessages(new ArrayList<>());
		conver.getMessages().add(message);
		_ConverationRepository.save(conver);
		_IMessageNotificationService.envioNotificacion(message);
		return ResponseEntity.status(HttpStatus.CREATED).body("se creó el mensaje nuevo y la conversacion nueva");
	}
	@Override
	public Conversation findConversationById(String id) {
		return _ConverationRepository.findById(id).orElse(new Conversation());
	}
	@Override
	public List<ConversationDTO> findAllConversations() {
		return _ConverationRepository.findAllConversations().stream().map(con->{
			Message m = con.getLastMessage();
			return new ConversationDTO(con.getChatId(),
					m.getSms(), 
					m.getFecha().equals(LocalDate.now().format(formatter))? m.getHora(): m.getFecha());
		}).collect(Collectors.toList());
	}

}
