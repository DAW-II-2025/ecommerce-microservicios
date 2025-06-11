package pe.edu.cibertec.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
	private String chatId;
	private String sms;
	private String sender;
	private String fecha;	
	private String hora;
}
