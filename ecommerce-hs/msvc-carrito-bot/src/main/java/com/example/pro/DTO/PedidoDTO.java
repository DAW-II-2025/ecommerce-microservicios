package com.example.pro.DTO;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PedidoDTO {
    private String distrito;
    private String direccion;
    private String referencia;
    @SerializedName("nombre_receptor")
    private String nombreReceptor;
    private String telefono;
}
