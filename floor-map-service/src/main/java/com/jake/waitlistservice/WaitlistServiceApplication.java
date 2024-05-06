package com.jake.waitlistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.jake.datacorelib.floormap"})
@EntityScan({"com.jake.datacorelib.floormap", "com.jake.datacorelib.restaurant", "com.jake.datacorelib.servicetype", "com.jake.datacorelib.serviceschedule"})
public class WaitlistServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaitlistServiceApplication.class, args);
    }
}
