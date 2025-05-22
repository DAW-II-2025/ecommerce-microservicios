package com.example.pro.services.Impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.example.pro.model.Cliente;
import com.example.pro.model.Venta;
import com.example.pro.services.IReporteServices;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReporteService implements IReporteServices {

    @Override
    public byte[] generarBoleta(Venta ventaSaved) throws JRException, IOException {
	ObjectMapper mapper = new ObjectMapper();
	InputStream logo = this.getClass().getResourceAsStream("/static/img/logo.jpeg");
	JRBeanCollectionDataSource detalle = new JRBeanCollectionDataSource((Collection<?>) ventaSaved.getDetalles());
	Cliente cli = ventaSaved.getCli();
	Map<String, Object> params = new HashMap<>(mapper.convertValue(ventaSaved, Map.class));
	params.put("REPORT_LOCALE", new Locale("es", "ES"));
	params.put("detalle", detalle);
	params.put("logo", logo);
	params.put("nombre", cli.getNombres());
	params.put("apellidos", cli.getApellidos());
	params.put("telefono", cli.getTelefono());
	params.put("dni", cli.getDni());
	JasperPrint jasperPrint = JasperFillManager.fillReport(this.getClass().getResourceAsStream("/reports/boleta.jasper"),
		params, new JREmptyDataSource());

	BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0, 1.6f);

	// Convertir a byte[]
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ImageIO.write(image, "png", baos);
	return baos.toByteArray();
    }

}
