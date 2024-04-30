package com.jake.reservationservice.controller;

import com.jake.reservationservice.dto.AvailableReservationsDTO;
import com.jake.reservationservice.dto.ReservationDTO;
import com.jake.reservationservice.jpa.domain.Reservation;
import com.jake.reservationservice.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final ReservationService service;

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @GetMapping(value = "/reservation")
    public ResponseEntity<List<Reservation>> getAll(Authentication authenticationToken) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());

        List<Reservation> reservations = service.findAllByBusinessId(getBusinessId(jwt));
        log.info("Found Reservations <{}>", reservations);
        return ResponseEntity.ok(reservations);
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @GetMapping(value = "/available")
    public ResponseEntity<Set<LocalTime>> getAllAvailable(Authentication authenticationToken, AvailableReservationsDTO availableReservationsDTO) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());

        Set<LocalTime> reservationsTimes = service.findAllAvailableReservationsForDate(getBusinessId(jwt), availableReservationsDTO.getReservationDay(), availableReservationsDTO.getPartySize());
        log.info("Found Available Reservations Times <{}>", reservationsTimes);
        return ResponseEntity.ok(reservationsTimes);
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PostMapping(value = "/reservation")
    public ResponseEntity<HttpStatus> add(Authentication authenticationToken, @RequestBody ReservationDTO reservationDTO) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();

        log.info("Adding reservation for business ID <{}>", getBusinessId(jwt));
        service.add(reservationDTO, getBusinessId(jwt));

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PutMapping(value = "/reservation")
    public ResponseEntity<HttpStatus> edit(Authentication authenticationToken, @RequestBody ReservationDTO reservationDTO) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();

        log.info("Editing reservation for business ID <{}>", getBusinessId(jwt));
        service.edit(reservationDTO);

        return ResponseEntity.ok().build();
    }

    private long getBusinessId(Jwt jwt) {
        return Long.valueOf(jwt.getClaimAsString("businessId"));
    }
}
