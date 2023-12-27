package com.jake.frontendapi.controller;

import com.jake.frontendapi.dto.AuthTokenDTO;
import com.jake.frontendapi.dto.UserCredentialDTO;
import com.jake.frontendapi.dto.WaitListItemDTO;
import com.jake.frontendapi.service.AuthService;
import com.jake.frontendapi.service.WaitListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final AuthService authService;
    private final WaitListService waitlistService;

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value = "/signin")
    public ResponseEntity<HttpStatus> signIn(@RequestBody UserCredentialDTO userCredentialDTO, HttpServletResponse httpResponse) throws UnsupportedEncodingException {
        log.info("SignIn <{}>", userCredentialDTO);

        Optional<AuthTokenDTO> authTokenDTO = authService.signIn(userCredentialDTO);

        if (authTokenDTO.isPresent()) {
            log.info("Signed In.");
            Cookie accessTokenCookie = new Cookie("accessToken", authTokenDTO.get().getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            httpResponse.addCookie(accessTokenCookie);
            Cookie refreshTokenCookie = new Cookie("refreshToken", authTokenDTO.get().getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            httpResponse.addCookie(refreshTokenCookie);
            return ResponseEntity.ok().build();
        } else {
            // Return 403 Forbidden when authentication fails
            log.info("Unauthorized.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value ="/signup")
    public ResponseEntity<HttpStatus> signUp(@RequestBody UserCredentialDTO userCredentialDTO, HttpServletResponse httpResponse) throws UnsupportedEncodingException {
        log.info("/signup <{}>", userCredentialDTO);
        Optional<AuthTokenDTO> optionalAuthTokenDTO = authService.signUp(userCredentialDTO);
        if(optionalAuthTokenDTO.isEmpty()) {
            log.info("Signup failed");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Signed Up.");
        Cookie accessTokenCookie = new Cookie("accessToken", optionalAuthTokenDTO.get().getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        httpResponse.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", optionalAuthTokenDTO.get().getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        httpResponse.addCookie(refreshTokenCookie);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value = "/validate")
    public ResponseEntity<HttpStatus> validate(HttpServletRequest httpRequest) {
        log.info("/validate <{}>", httpRequest);

        Optional<Cookie> optionalCookie = getAccessTokenCookie(httpRequest.getCookies());
        if(optionalCookie.isEmpty()) {
            log.info("Unathorized. No Access token found.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String accessToken = optionalCookie.get().getValue();
        boolean isValidated = authService.validateJwt(accessToken);
        log.info("isValidated <{}>", isValidated);

        if(!isValidated) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value = "/refresh")
    public ResponseEntity<HttpStatus>  refresh(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        log.info("/refresh <{}>", httpRequest);
        Optional<Cookie> optionalCookie = getRefreshTokenCookie(httpRequest.getCookies());
        if(optionalCookie.isEmpty()) {
            log.info("No refresh token found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String refreshToken = optionalCookie.get().getValue();

        Optional<AuthTokenDTO> authTokenDTO = authService.refresh(refreshToken);
        if (authTokenDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Cookie accessTokenCookie = new Cookie("accessToken", authTokenDTO.get().getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        httpResponse.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", authTokenDTO.get().getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        httpResponse.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @GetMapping(value="/waitList")
    public ResponseEntity<List<WaitListItemDTO>> getAllWaitListItems(HttpServletRequest httpRequest) {
        log.info("getAllWaitListItems <{}>", httpRequest);

        Optional<Cookie> accessTokenCookie = getAccessTokenCookie(httpRequest.getCookies());
        if(accessTokenCookie.isEmpty()) {
            log.info("get /waitlist no access token found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of());
        }
        String jwt = accessTokenCookie.get().getValue();
        if(!authService.validateJwt(jwt)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(List.of());
        }

       List<WaitListItemDTO> waitListItems =  waitlistService.getAllWaitListItems(jwt);

        return ResponseEntity.ok(waitListItems);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // Replace with your allowed origin
    @PostMapping(value="/waitList")
    public ResponseEntity<HttpStatus> addWaitListItem(@RequestBody WaitListItemDTO waitListItemDTO, HttpServletRequest httpRequest) {
        log.info("addWaitListItem <{}>", httpRequest);

        Optional<Cookie> accessTokenCookie = getAccessTokenCookie(httpRequest.getCookies());
        if(accessTokenCookie.isEmpty()) {
            log.error("Unauthorized. No access token found.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String accessToken = accessTokenCookie.get().getValue();

        waitlistService.addWaitListItem(accessToken, waitListItemDTO);

        return ResponseEntity.ok().build();
    }

    private Optional<Cookie> getAccessTokenCookie(Cookie[] cookies) {
        if(cookies==null) {
            log.info("No access token cookie. Cookies is null.");
            return Optional.empty();
        }
        Arrays.stream(cookies).forEach(cookie->log.info(cookie.getName()));
        return Arrays.stream(cookies).filter(cookie->cookie.getName().equalsIgnoreCase("accessToken")).findFirst();
    }

    private Optional<Cookie> getRefreshTokenCookie(Cookie[] cookies) {
        if(cookies==null) {
            log.info("No refresh cookie. Cookies is null.");
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie->cookie.getName().equalsIgnoreCase("refreshToken")).findFirst();
    }

}
