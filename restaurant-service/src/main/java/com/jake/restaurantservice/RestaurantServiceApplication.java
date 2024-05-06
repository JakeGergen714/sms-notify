package com.jake.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.jake.datacorelib.restaurant", "com.jake.datacorelib.servicetype", "com.jake.datacorelib.serviceschedule"})
@EntityScan({"com.jake.datacorelib.restaurant", "com.jake.datacorelib.servicetype", "com.jake.datacorelib.serviceschedule"})
public class RestaurantServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestaurantServiceApplication.class, args);
	}
}
