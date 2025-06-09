package pe.edu.cibertec.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document
@Data
public class Message {
	@Id
	private String messageId;	
	private String sms;
	private String sender;
	private String fecha;	
	private String hora;
}
