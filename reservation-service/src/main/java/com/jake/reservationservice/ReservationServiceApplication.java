package com.jake.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point for the Reservation Service application.
 * This application sets up Spring Boot and configures JPA repositories and entity scanning
 * from the `datacorelib` package to support reservation and floor map management functionalities.
 */
@SpringBootApplication
@EnableJpaRepositories({"com.jake.datacorelib.reservation", "com.jake.datacorelib.floormap"})
@EntityScan({"com.jake.datacorelib.reservation", "com.jake.datacorelib.floormap"})
public class ReservationServiceApplication {
    /**
     * Starts the Spring Boot application.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }
}
