package pe.edu.cibertec.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
	private String chatId;
	private String sms;
	private String sender;
	private String fecha;	
	private String hora;
}
