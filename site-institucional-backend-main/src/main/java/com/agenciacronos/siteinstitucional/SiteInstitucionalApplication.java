package com.agenciacronos.siteinstitucional;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Agencia Cronos API", version = "2.0", description = "Site institucional"))
public class SiteInstitucionalApplication {

	private static Logger logger = LoggerFactory.getLogger(SiteInstitucionalApplication.class);

	public static void main(String[] args) {

		logger.info("Iniciando a api para cadastro de serviços, posts e integrantes");

		SpringApplication.run(SiteInstitucionalApplication.class, args);

		logger.info("API iniciada e pronta para receber requisições!!");
	}



}
