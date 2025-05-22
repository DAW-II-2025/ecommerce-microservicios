package com.example.pro.DTO;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VentaAndDetalles {
    
    @SerializedName("pedido_dto")

    private PedidoDTO pedidoDTO;
    @SerializedName("pago_dto")
    private PagoDTO pagoDTO;
    @SerializedName("venta_dto")
    private VentaDTO ventaDTO;
    @SerializedName("detalles_dto")
    private List<DetalleDTO> detallesDTO;

}
