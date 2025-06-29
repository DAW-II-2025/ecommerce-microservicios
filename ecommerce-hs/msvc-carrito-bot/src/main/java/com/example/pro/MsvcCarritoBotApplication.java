package com.example.pro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mercadopago.MercadoPago;
@EnableFeignClients(basePackages = "com.example.pro.client")
@EnableAsync
@SpringBootApplication
public class MsvcCarritoBotApplication implements CommandLineRunner {
    	@Value("${MP_ACCESS_TOKEN}")
    	private String accesToken;
	public static void main(String[] args) {
		SpringApplication.run(MsvcCarritoBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	    MercadoPago.SDK.setAccessToken(accesToken);	    
	}
}
