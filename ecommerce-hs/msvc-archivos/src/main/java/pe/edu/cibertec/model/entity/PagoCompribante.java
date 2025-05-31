package pe.edu.cibertec.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "comprobantes")
public class PagoCompribante{
	@Id
	private String idArchivo;
	private Integer idPago;
	
}
