package pe.edu.cibertec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.model.ConversationDTO;
import pe.edu.cibertec.model.entity.Conversation;
import pe.edu.cibertec.model.entity.Message;
import pe.edu.cibertec.service.IMessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/archivos")
public class MessageController {
	@Autowired
	private IMessageService service;
	
	@PostMapping("/message")
	public ResponseEntity<?> postMethodName(@RequestBody Message message) {
		try {			
			return service.guardarMensaje(message);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/conversation/{id}")
	public Conversation getMethodName(@PathVariable String id) {
		return service.findConversationById(id);
	}
	
	@GetMapping("/conversations")
	public List<ConversationDTO> getConversations() {
		return service.findAllConversations();
	}
	
	

}
