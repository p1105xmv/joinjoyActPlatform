package com.joinjoy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication

public class JoinJoyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoinJoyApplication.class, args);
	}

}
