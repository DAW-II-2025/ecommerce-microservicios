package pe.edu.cibertec.service;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

public interface GridFsService {

	String saveFile(MultipartFile file) throws IOException;

	InputStreamResource downloadFile(String id) throws IOException;

}
