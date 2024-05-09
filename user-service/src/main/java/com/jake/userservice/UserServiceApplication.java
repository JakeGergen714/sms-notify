package com.jake.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.jake.datacorelib.business","com.jake.datacorelib.restaurant", "com.jake.datacorelib.user", "com.jake.datacorelib.subscription"})
@EntityScan({"com.jake.datacorelib.business","com.jake.datacorelib.restaurant", "com.jake.datacorelib.user", "com.jake.datacorelib.subscription"})
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
