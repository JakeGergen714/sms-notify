package com.jake.waitlistservice.controller;

import com.jake.waitlistservice.dto.WaitListItemDTO;
import com.jake.waitlistservice.jpa.domain.WaitListItem;
import com.jake.waitlistservice.service.WaitListService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final WaitListService service;

    @CrossOrigin(origins = "http://localhost/:8090", allowCredentials = "true") // Replace with your allowed origin
    @GetMapping(value="/waitlist")
    public ResponseEntity<List<WaitListItem>> getAllWaitListItems(Authentication authenticationToken) {
        Jwt jwt = (Jwt)authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        log.info(jwt.getClaims());

        List<WaitListItem> items = service.findAllForUser(username);
        log.info("Found Items <{}>", items);
        return ResponseEntity.ok(items);
    }

    @CrossOrigin(origins = "http://localhost/:8090", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value="/waitlist")
    public ResponseEntity<HttpStatus> addWaitListItem(Authentication authenticationToken, @RequestBody WaitListItemDTO waitListItemDTO) {
        Jwt jwt = (Jwt)authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        
        log.info("Adding waitlist item for username <{}>", username);
        service.add(waitListItemDTO, username);

        return ResponseEntity.ok().build();
    }
}
