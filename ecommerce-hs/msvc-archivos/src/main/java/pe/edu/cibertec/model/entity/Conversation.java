package pe.edu.cibertec.model.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Conversation {
	@Id
	private String chatId;	
	private List<Message> messages;
	
	public Message getLastMessage() {
		return messages.isEmpty()? null: messages.get(messages.size()-1);
	}
}
