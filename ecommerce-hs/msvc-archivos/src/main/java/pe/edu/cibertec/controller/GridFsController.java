package pe.edu.cibertec.controller;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import pe.edu.cibertec.service.GridFsService;

@RestController
@RequestMapping("/gridfs")
public class GridFsController {
	private final GridFsService gridFsService;

	public GridFsController(GridFsService gridFsService) {
		this.gridFsService = gridFsService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		String id = gridFsService.saveFile(file);
		return ResponseEntity.ok("Archivo subido exitosamente. id: " + id);
	}

	@GetMapping("/{id}")
	public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id) throws IOException {
		InputStreamResource file = gridFsService.downloadFile(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + "\"")
				.body(file);
	}
}
