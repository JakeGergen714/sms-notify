package com.jake.reservationservice.controller;

import com.jake.reservationservice.dto.AvailableReservationDTO;
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

import java.util.List;

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
    public ResponseEntity<List<AvailableReservationDTO>> getAllAvailable(Authentication authenticationToken) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());

        List<Reservation> reservations = service.findAllByBusinessId(getBusinessId(jwt));
        log.info("Found Reservations <{}>", reservations);
        return null;
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

    private long getBusinessId(Jwt jwt) {
        return Long.valueOf(jwt.getClaimAsString("businessId"));
    }
}
