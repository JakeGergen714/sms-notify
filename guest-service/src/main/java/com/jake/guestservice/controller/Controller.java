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

    //seat guest
    //un-seat guest. (added to table by accident. return to waitlist / reservation list
    //move guest other table
    //guest table complete (done eating)

    //add guest -- waitlist guest, or an upcoming reservation guest
    //edit guest -- guest is seated / guest is unseated
    //remove guest -- guest is finished

    //a guest can be in 3 states
    //unseated -- waiting to be seated, or waiting for reservation
    //seated -- guest is seated
    //completed -- guest is finished and has left

    private long getBusinessId(Authentication authentication) {
        return Long.valueOf(((Jwt) authentication.getPrincipal()).getClaimAsString("businessId"));
    }
}
