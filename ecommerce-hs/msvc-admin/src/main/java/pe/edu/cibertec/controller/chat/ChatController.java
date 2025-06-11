package pe.edu.cibertec.controller.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pe.edu.cibertec.dto.chat.Conversation;
import pe.edu.cibertec.service.chat.IChatService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
  

@Controller
@RequestMapping("/chat")
public class ChatController {
	@Autowired
	private IChatService _service;
		
	@GetMapping
	public String getMethodName(Model model) {
		model.addAttribute("chats", _service.findConversation());
		return "chat/interfaz";
	}
	
	@GetMapping("/{chatId}")
	@ResponseBody
	public Conversation getMethodName(@PathVariable String chatId) {
		return _service.findConversationById(chatId);
	
	}
	
	
}
