package com.example.pro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.mercadopago.MercadoPago;
@EnableFeignClients(basePackages = "com.example.pro.client")
@SpringBootApplication
public class CarritoadminApplication implements CommandLineRunner {
    	@Value("${MP_ACCESS_TOKEN}")
    	private String accesToken;
	public static void main(String[] args) {
		SpringApplication.run(CarritoadminApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	    MercadoPago.SDK.setAccessToken(accesToken);	    
	}
}
