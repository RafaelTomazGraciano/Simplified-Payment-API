package com.simplifiedpaymentapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Simplified Payment API", version = "1", description = "API for simplified payment transactions between users"))
@SpringBootApplication
public class SimplifiedPaymentAPIApplication {

	public static void main(String[] args) {

		SpringApplication.run(SimplifiedPaymentAPIApplication.class, args);
	}

}
