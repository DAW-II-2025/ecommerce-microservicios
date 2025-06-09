package pe.edu.cibertec.model.chat;

import lombok.Data;

@Data
public class Message {
	private String sms;
	private String sender;
	private String chatId;
	private String fecha;	
	private String hora;
}
