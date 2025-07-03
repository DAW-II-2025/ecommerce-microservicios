package pe.edu.cibertec.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pe.edu.cibertec.model.entity.Message;


@FeignClient(name = "msvc-admin", url = "${msvc.admin}")
public interface IAdminClient {
	
	@PostMapping("/chat/publish")
	ResponseEntity<Map<String, Object>> publish(@RequestBody Message message);

}
