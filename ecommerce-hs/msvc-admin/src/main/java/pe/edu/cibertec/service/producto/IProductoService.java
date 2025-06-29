package pe.edu.cibertec.service.producto;

import org.springframework.web.multipart.MultipartFile;

public interface IProductoService {
    void procesarExcel(MultipartFile archivo) throws Exception;
}
