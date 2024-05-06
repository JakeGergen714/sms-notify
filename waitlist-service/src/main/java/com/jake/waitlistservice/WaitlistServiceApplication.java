package com.jake.waitlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.jake.datacorelib.waitlist"})
@EntityScan({"com.jake.datacorelib.waitlist"})
@SpringBootApplication
public class WaitlistServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(WaitlistServiceApplication.class, args);
	}
}
