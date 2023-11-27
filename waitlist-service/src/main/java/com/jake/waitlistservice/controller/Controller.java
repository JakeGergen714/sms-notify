package com.jake.waitlistservice.controller;

import com.jake.waitlistservice.dto.AuthorizationDTO;
import com.jake.waitlistservice.dto.WaitListItemDTO;
import com.jake.waitlistservice.jpa.domain.WaitListItem;
import com.jake.waitlistservice.service.AuthService;
import com.jake.waitlistservice.service.WaitListService;
import com.jake.waitlistservice.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final WaitListService service;
    private final AuthService authService;

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @GetMapping(value="/waitList")
    public ResponseEntity<List<WaitListItem>> getAllWaitListItems(@RequestHeader String accessToken) {
        log.info("getAllWaitListItems <{}>", accessToken);

        /*Optional<Cookie> accessTokenCookie = getAccessTokenCookie(httpRequest.getCookies());
        if(accessTokenCookie.isEmpty()) {
            log.info("get /waitlist no access token found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }
        String jwt = accessTokenCookie.get().getValue();
        if(!authService.validateJwt(jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }*/

        if(!authService.validateJwt(accessToken)) {
            log.info("Validation Failed");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Validation Successful");

        AuthorizationDTO authorizationDTO = JwtUtil.decodeJwt(accessToken);
        log.info("Authorization DTO <{}>", authorizationDTO);
        List<WaitListItem> items = service.findAllForUser(authorizationDTO.getUsername());
        log.info("Found Items <{}>", items);
        return ResponseEntity.ok(items);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value="/waitList")
    public ResponseEntity<HttpStatus> addWaitListItem(@RequestHeader String accessToken, @RequestBody WaitListItemDTO waitListItemDTO) {
        log.info("addWaitListItem <{}>, <{}>", accessToken, waitListItemDTO);

        /*log.info("addWaitListItem <{}>", waitListItemDTO);
        Optional<Cookie> accessTokenCookie = getAccessTokenCookie(httpRequest.getCookies());
        if(accessTokenCookie.isEmpty()) {
            log.error("post /waitlist no access token found");
            return;
        }*/
        if(!authService.validateJwt(accessToken)) {
            log.info("Validation Failed");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Validation Successful");

        AuthorizationDTO authorizationDTO = JwtUtil.decodeJwt(accessToken);
        log.info("Authorization DTO <{}>", authorizationDTO);

        log.info("Adding waitlist item for username <{}>", authorizationDTO.getUsername());
        service.add(waitListItemDTO, authorizationDTO.getUsername());

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
