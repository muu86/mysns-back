package com.mj.mysns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MySnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySnsApplication.class, args);
	}

}
