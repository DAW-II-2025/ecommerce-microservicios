package pe.edu.cibertec.dto.chat;

import java.util.List;

import pe.edu.cibertec.model.chat.Message;

public record Conversation(
		String chatId,
		List<Message> messages) {

}
