package com.jake.reservationservice.controller;

import com.jake.datacorelib.reservation.jpa.Reservation;
import com.jake.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final ReservationService service;


    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/available")
    public ResponseEntity<Map<LocalDate, Set<LocalTime>>> getAllAvailable(Authentication authenticationToken, @RequestParam Long restaurantId, @RequestParam LocalDate fromDay, @RequestParam LocalDate toDay, int partySize) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());

        return ResponseEntity.ok(service.getAllAvailableReservationsBetweenInclusive(restaurantId, fromDay, toDay, partySize));
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/reservations")
    public ResponseEntity<List<Reservation>> getReservationsOnDate(Authentication authenticationToken, @RequestParam Long restaurantId, @RequestParam LocalDate date) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());

        return ResponseEntity.ok(service.getAllReservationForDate(restaurantId, date));
    }

    private long getBusinessId(Jwt jwt) {
        return Long.valueOf(jwt.getClaimAsString("businessId"));
    }
}
