package pe.edu.cibertec.service.producto.Impl;

import org.springframework.stereotype.Service;
import pe.edu.cibertec.service.producto.IProductoService;


@Service
public class ProductoService implements IProductoService {
	
	private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
	
    public void procesarExcel(MultipartFile archivo) throws Exception {
        try (InputStream is = archivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet hoja = workbook.getSheetAt(0);

            for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                Row fila = hoja.getRow(i);
                if (fila == null || fila.getCell(0) == null) continue; // salta filas vacias

                String descripcion = fila.getCell(0).getStringCellValue().trim();
                double precio = fila.getCell(1).getNumericCellValue();
                int stock = (int) fila.getCell(2).getNumericCellValue();
                String categoria = fila.getCell(3).getStringCellValue().trim();
                String imagen = fila.getCell(4).getStringCellValue().trim();
                String estado = fila.getCell(5).getStringCellValue().trim();

                Producto producto = new Producto(descripcion, precioUnidad, stock, categoria, imagen, estado);
                productoRepository.save(producto);
            }
        } catch (Exception e) {
            throw new Exception("Error al procesar el archivo Excel", e);
        }
    }
}
