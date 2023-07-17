package com.shiromi.ashiura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class AshiuraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AshiuraApplication.class, args);
	}

}
