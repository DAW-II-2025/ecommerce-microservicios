package com.example.pro.services.Impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pro.DTO.DetalleDTO;
import com.example.pro.DTO.PagoDTO;
import com.example.pro.DTO.PedidoDTO;
import com.example.pro.DTO.VentaAndDetalles;
import com.example.pro.Repository.IClienteRepository;
import com.example.pro.Repository.IDetalleRepository;
import com.example.pro.Repository.IProductoRepository;
import com.example.pro.Repository.IVentaRepository;
import com.example.pro.model.Detalle;
import com.example.pro.model.EstadoPedido;
import com.example.pro.model.Pago;
import com.example.pro.model.Pedido;
import com.example.pro.model.Producto;
import com.example.pro.model.Venta;
import com.example.pro.services.IUsuarioServices;
import com.example.pro.services.IVentaServices;

@Service
public class VentaServices implements IVentaServices {
    private Logger log = LoggerFactory.getLogger(VentaServices.class);

    private IProductoRepository _productoRepository;
    private IVentaRepository _ventaRepository;
    private IDetalleRepository _detalleRepository;
    private IClienteRepository _clienteRepository;
    private ProductoServices productoServices;
    private IUsuarioServices iUsuarioServices;

    public VentaServices(IProductoRepository productoRepository, IClienteRepository clienteRepository,
	    IVentaRepository ventaRepository, IDetalleRepository detalleRepository, ProductoServices productoServices, 
	    IUsuarioServices iUsuarioServices) {
	_productoRepository = productoRepository;
	_clienteRepository = clienteRepository;
	_ventaRepository = ventaRepository;
	_detalleRepository = detalleRepository;
	this.productoServices = productoServices;
	this.iUsuarioServices = iUsuarioServices;
	
    }

    @Override
    public List<Venta> GetAllVentas() {
	return _ventaRepository.findAll();
    }

    @Override
    public Venta SaveVenta(Venta entity) {
	Venta ventaSaved = _ventaRepository.save(entity);
	return ventaSaved;
    }

    @Override
    public Venta FindVentaById(int id) {
	Optional<Venta> rowInDB = _ventaRepository.findById(id);
	if (rowInDB.isPresent()) {
	    return rowInDB.get();
	} else {
	    return new Venta();
	}
    }

    @Override
    public Integer deleteVenta(Integer id) {
	Optional<Venta> optionalVenta = _ventaRepository.findById(id);
	if (optionalVenta.isPresent()) {
	    _ventaRepository.deleteById(id);
	    return 1;
	} else {
	    return 0;
	}
    }

    @Override
    public Venta SaveVentaAndDetalles(VentaAndDetalles entity) {
	Venta ventaToSave = new Venta();
	System.err.println(entity.getVentaDTO().getCli());
	ventaToSave.setCli(_clienteRepository.findbyCorreo(entity.getVentaDTO().getCli()).orElseThrow());
	ventaToSave.setFechaVenta(entity.getVentaDTO().getFechaVenta());
	ventaToSave.setMonto(entity.getVentaDTO().getMonto());

	PagoDTO pagodto = entity.getPagoDTO();
	Pago pago = new Pago(pagodto.getPaymentId(), ventaToSave, pagodto.getEstado(), pagodto.getMetodo());

	PedidoDTO pedidodto = entity.getPedidoDTO();
	Pedido pedido = new Pedido(null, pedidodto.getDistrito(), pedidodto.getDireccion(), pedidodto.getReferencia(),
		pedidodto.getNombreReceptor(), pedidodto.getTelefono(),EstadoPedido.PENDIENTE, ventaToSave);

	ventaToSave.setPago(pago);
	ventaToSave.setPedido(pedido);
	try {
	    Venta VentaSaved = new Venta();
	    VentaSaved = _ventaRepository.save(ventaToSave);

	    for (DetalleDTO DetalleDTO : entity.getDetallesDTO()) {
		Detalle detToSave = new Detalle();
		detToSave.setProducto(_productoRepository.findById(DetalleDTO.getProducto()).get());
		detToSave.setCant(DetalleDTO.getCant());
		detToSave.setVenta(VentaSaved);
		_detalleRepository.save(detToSave);
		if (detToSave.getProducto() != null) {
		    Producto producto = detToSave.getProducto();
		    producto.setStock(producto.getStock() - detToSave.getCant());
		    productoServices.updateProducto(producto.getIdProducto(), producto);
		}
	    }

	    return VentaSaved;
	} catch (Exception e) {
	    log.error("error al generar la venta con le pago: " + e.getMessage());
	    return null;
	}
    }

    @Override
    public List<Venta> misVentas() {
	return _ventaRepository.findByCli(iUsuarioServices.getUsuarioActual());
    }

    @Override
    public Venta findByPago(Pago pago) {
	return _ventaRepository.findByPago(pago).orElseThrow();
    }
}
