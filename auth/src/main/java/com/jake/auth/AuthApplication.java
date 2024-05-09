package com.jake.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories({"com.jake.datacorelib.business","com.jake.datacorelib.restaurant", "com.jake.datacorelib.user", "com.jake.datacorelib.subscription"})
@EntityScan({"com.jake.datacorelib.business","com.jake.datacorelib.restaurant", "com.jake.datacorelib.user", "com.jake.datacorelib.subscription"})
@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
