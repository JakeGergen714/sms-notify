package com.jake.waitlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WaitlistServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(WaitlistServiceApplication.class, args);
	}
}
