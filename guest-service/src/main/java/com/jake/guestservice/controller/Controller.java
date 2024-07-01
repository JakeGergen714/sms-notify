package com.jake.guestservice.controller;

import com.jake.datacorelib.guest.jpa.Guest;
import com.jake.datacorelib.guest.jpa.GuestStatus;
import com.jake.guestservice.service.GuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final GuestService service;

    /**
     * Retrieves a list of guests filtered by status.
     *
     * @param status filter by guest status.
     * @return List of guests filtered by the given status if provided, otherwise all guests.
     */
    @GetMapping("/guests")
    public ResponseEntity<List<Guest>> getGuestsByStatus(Authentication authentication, GuestStatus status) {
        List<Guest> guests = service.findGuestsByStatus(getBusinessId(authentication), status);
        return ResponseEntity.ok(guests);
    }

    // The guest data base stores all future and past guest information. Name, telephone etc...
    // A guest is unique by its telephone number.
    //

    //seat guest
    //un-seat guest. (added to table by accident. return to waitlist / reservation list
    //move guest other