package pe.edu.cibertec.service.producto.Impl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.dto.producto.ProductoDTO;
import pe.edu.cibertec.feign.ProductoClient;
import pe.edu.cibertec.service.producto.IProductoService;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoClient _Productoclient;

    @Override
    public void procesarExcel(MultipartFile archivo) throws Exception {
        try (InputStream is = archivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet hoja = workbook.getSheetAt(0);

            for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                Row fila = hoja.getRow(i);
                if (fila == null || fila.getCell(0) == null) continue; // salta filas vacias

                String descripcion = fila.getCell(0).getStringCellValue().trim();
                double precioUnidad = fila.getCell(1).getNumericCellValue();
                int stock = (int) fila.getCell(2).getNumericCellValue();
                String categoria = fila.getCell(3).getStringCellValue().trim();
                String imagen = fila.getCell(4).getStringCellValue().trim();
                String estado = fila.getCell(5).getStringCellValue().trim();

                ProductoDTO producto = new ProductoDTO(null,descripcion, precioUnidad, stock, categoria, imagen, estado);
                _Productoclient.crearProducto(producto);
            }
        } catch (Exception e) {
            throw new Exception("Error al procesar el archivo Excel", e);
        }
    }
}
