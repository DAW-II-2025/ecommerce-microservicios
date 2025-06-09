package pe.edu.cibertec.controller.chat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.model.chat.Message;
import pe.edu.cibertec.service.IMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/chat")
public class MessageRestController {
	@Autowired
	private IMessageService _service;
	
	@PostMapping("/publish")
	public ResponseEntity<?> postMethodName(@RequestBody Message message) {		
		return _service.publicarMensaje(message);
		
	}
	
}
