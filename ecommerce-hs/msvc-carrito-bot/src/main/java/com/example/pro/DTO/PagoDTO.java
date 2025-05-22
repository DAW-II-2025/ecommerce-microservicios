package com.example.pro.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PagoDTO {
    private String paymentId;
    private String metodo;
    private String estado;
}
