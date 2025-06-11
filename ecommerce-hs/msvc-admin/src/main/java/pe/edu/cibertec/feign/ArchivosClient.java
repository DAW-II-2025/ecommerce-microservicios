package pe.edu.cibertec.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pe.edu.cibertec.dto.chat.Conversation;
import pe.edu.cibertec.dto.chat.ConversationDTO;
import pe.edu.cibertec.model.chat.Message;


@FeignClient(name = "msvc-archivos", url = "localhost:8083")
public interface ArchivosClient {

	@PostMapping("/archivos/message")
	ResponseEntity<?> guardarMensaje(@RequestBody Message message);
	
	@GetMapping("/archivos/conversation/{id}")
	Conversation obtenerConversacion(@PathVariable String id);

	@GetMapping("/archivos/conversations")
	List<ConversationDTO> obtenerConversaciones();
	
}
