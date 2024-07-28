package com.sokpulee.crescendo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CrescendoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrescendoApplication.class, args);
	}

}
