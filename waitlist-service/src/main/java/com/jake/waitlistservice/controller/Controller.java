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

    @CrossOrigin(origins = "http://192.168.1.241/:8090", allowCredentials = "true") // Replace with your allowed origin
    @GetMapping(value="/waitList")
    public ResponseEntity<List<WaitListItem>> getAllWaitListItems(Authentication authenticationToken) {
        Jwt jwt = (Jwt)authenticationToken.getPrincipal();
        log.info(jwt.getClaims());
      /*  String username = jwt.getClaimAsString("username");
        log.info("getAllWaitListItems <{}>", accessToken);

        log.info("Validation Successful");

        List<WaitListItem> items = service.findAllForUser(authorizationDTO.getUsername());
        log.info("Found Items <{}>", items);*/
        return ResponseEntity.ok(List.of(new WaitListItem()));
    }

    @CrossOrigin(origins = "http://192.168.1.241/:8090", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value="/waitList")
    public ResponseEntity<HttpStatus> addWaitListItem(@RequestHeader String accessToken, @RequestBody WaitListItemDTO waitListItemDTO) {
        log.info("addWaitListItem <{}>, <{}>", accessToken, waitListItemDTO);

        log.info("Validation Successful");



        //log.info("Adding waitlist item for username <{}>", authorizationDTO.getUsername());
        //service.add(waitListItemDTO, authorizationDTO.getUsername());

        return ResponseEntity.ok().build();
    }

    private Optional<Cookie> getAccessTokenCookie(Cookie[] cookies) {
        if(cookies==null) {
            return Optional.empty();
        }
        Arrays.stream(cookies).forEach(cookie->log.info(cookie.getName()));
        return Arrays.stream(cookies).filter(cookie->cookie.getName().equalsIgnoreCase("accessToken")).findFirst();
    }
}
