package com.example.pro.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentIntentDTO {
	public enum Currency{
		USD ,PEN, EUR;
	}
	
	private String descripcion;
	private int amount;
	private Currency currency;
}
