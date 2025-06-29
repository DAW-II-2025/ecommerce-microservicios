package pe.edu.cibertec.service.producto.Impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pe.edu.cibertec.dto.producto.ProductoDTO;
import pe.edu.cibertec.feign.ProductoClient;
import pe.edu.cibertec.service.producto.IProductoService;

import java.io.InputStream;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    ProductoClient productoclient;

    @Override
    public void procesarExcel(MultipartFile archivo) throws Exception {
        try (InputStream is = archivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet hoja = workbook.getSheetAt(0);
            int filasExitosas = 0;
            int filasConError = 0;

            for (int i = 1; i <= hoja.getLastRowNum(); i++) {
                Row fila = hoja.getRow(i);

                if (fila == null || esFilaVacia(fila)) {
                    continue;
                }

                try {
                    String descripcion = obtenerValorString(fila, 0);
                    if (descripcion.isEmpty()) {
                        System.out.println("Fila " + (i + 1) + ": Descripción vacía, saltando...");
                        filasConError++;
                        continue;
                    }
                    double precioUnidad = obtenerValorNum(fila, 1);
                    int stock = (int) obtenerValorNum(fila, 2);
                    String categoria = obtenerValorString(fila, 3);
                    String imagen = obtenerValorString(fila, 4);
                    String estado = obtenerValorString(fila, 5);

                    if (!estado.equals("A") && !estado.equals("D")) {
                        estado = "A"; //si pone otra cosa A por defecto
                    }

                    ProductoDTO producto = new ProductoDTO(
                            null, descripcion, precioUnidad, stock, categoria, imagen, estado
                    );

                    // Intentar crear el producto
                    ResponseEntity<?> resultado = productoclient.crearProducto(producto);
                    if (resultado != null) {
                        filasExitosas++;
                        System.out.println("Producto creado exitosamente: " + descripcion);
                    } else {
                        filasConError++;
                        System.out.println("Error al crear producto: " + descripcion);
                    }

                } catch (Exception e) {
                    filasConError++;
                    System.out.println("Error en fila " + (i + 1) + ": " + e.getMessage());
                }
            }
            if (filasExitosas == 0) {
                throw new Exception("No se pudo procesar ningún producto del archivo");
            }
        } catch (Exception e) {
            throw new Exception("Error al procesar el archivo Excel: " + e.getMessage(), e);
        }
    }

    private boolean esFilaVacia(Row fila) {
        for (int j = 0; j < 6; j++) {
            Cell celda = fila.getCell(j);
            if (celda != null && celda.getCellType() != CellType.BLANK) {
                String valor = celda.toString().trim();
                if (!valor.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String obtenerValorString(Row fila, int columna) {
        Cell celda = fila.getCell(columna);
        if (celda == null) return "";

        switch (celda.getCellType()) {
            case STRING:
                return celda.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) celda.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(celda.getBooleanCellValue());
            default:
                return "";
        }
    }

    private double obtenerValorNum(Row fila, int columna) {
        Cell celda = fila.getCell(columna);
        if (celda == null) return 0.0;

        switch (celda.getCellType()) {
            case NUMERIC:
                return celda.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(celda.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }
}