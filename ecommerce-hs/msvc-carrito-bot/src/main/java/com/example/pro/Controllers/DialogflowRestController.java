package com.example.pro.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pro.Repository.IProductoRepository;
import com.example.pro.model.Producto;
import com.example.pro.services.IProductoServices;

@RestController
@RequestMapping("/dialogflow")
public class DialogflowRestController {
    @Autowired
    private IProductoRepository _IProductoServices;

    @PostMapping
    public ResponseEntity<Map<String, Object>> handleWebhook(@RequestBody Map<String, Object> request) {
	Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
	String intentName = (String) ((Map<String, Object>) queryResult.get("intent")).get("displayName");
	Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");
	System.out.println("request: " + request.toString());
	String nombreProducto = parameters.get("producto").toString();
	String fulfillmentText;
	fulfillmentText = "";

	// Lógica según el intent
	if ("ConsultarProducto".equals(intentName) && !nombreProducto.isBlank() && nombreProducto != null) {
	    List<Producto> proOp = _IProductoServices.findByDescripcionContainingIgnoreCase(nombreProducto);
	    if (!proOp.isEmpty()) {
		for (Producto pro : proOp)
		    fulfillmentText += pro.getInfoWhatsapp();
		fulfillmentText += "hecha un vistaso a nuestro catálogo de productos: "
			+ "https://proyectocarritoantonitrejo.netlify.app/productos";

	    }
	} else {
	    fulfillmentText = "no entendí, puedes repetirlo";
	}

	// Crear respuesta
	Map<String, Object> response = new HashMap<>();
	response.put("fulfillmentText", fulfillmentText);

	return ResponseEntity.ok(response);
    }

}
