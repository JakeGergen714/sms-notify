package com.jake.auth.controller;

import com.jake.auth.dto.AuthTokenDTO;
import com.jake.auth.dto.UserCredentialDTO;
import com.jake.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Log4j2
public class Controller {
    private final AuthService authService;

    @PostMapping(value = "/signin")
    public ResponseEntity<AuthTokenDTO> signIn(@RequestBody UserCredentialDTO userCredentialDTO) throws UnsupportedEncodingException {
        log.info("SignIn <{}>", userCredentialDTO);

        Optional<AuthTokenDTO> authTokenDTO = authService.signIn(userCredentialDTO);

        if (authTokenDTO.isPresent()) {
            log.info("Signed In.");
            Cookie accessTokenCookie = new Cookie("accessToken", authTokenDTO.get().getAccessToken());
            accessTokenCookie.setHttpOnly(true);
            //httpResponse.addCookie(accessTokenCookie);
            Cookie refreshTokenCookie = new Cookie("refreshToken", authTokenDTO.get().getRefreshToken());
            refreshTokenCookie.setHttpOnly(true);
            //httpResponse.addCookie(refreshTokenCookie);
            return ResponseEntity.ok().body(authTokenDTO.get());
        } else {
            // Return 403 Forbidden when authentication fails
            log.info("Unauthorized.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping(value ="/signup")
    public ResponseEntity<AuthTokenDTO> signUp(@RequestBody UserCredentialDTO userCredentialDTO) throws UnsupportedEncodingException {
        log.info("/signup <{}>", userCredentialDTO);
        Optional<AuthTokenDTO> optionalAuthTokenDTO = authService.signUp(userCredentialDTO);
        if(optionalAuthTokenDTO.isEmpty()) {
            log.info("Signup failed");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthTokenDTO(null, null));
        }
        return ResponseEntity.ok().body(optionalAuthTokenDTO.get());
    }

    @PostMapping(value = "/validate")
    public ResponseEntity<HttpStatus> validate(@RequestHeader String accessToken) {
        log.info("/validate <{}>", accessToken);
        //Optional<Cookie> optionalCookie = getAccessTokenCookie(httpRequest.getCookies());
       /* if(optionalCookie.isEmpty()) {
            log.info("No Access token found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }*/
        boolean isValidated = authService.validateJwt(accessToken);
        log.info("isValidated <{}>", isValidated);
        if(!isValidated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<AuthTokenDTO>  refresh(@RequestHeader String refreshToken) {
        log.info("/refresh <{}>", refreshToken);
       /* Optional<Cookie> optionalCookie = getRefreshTokenCookie(httpRequest.getCookies());
        if(optionalCookie.isEmpty()) {
            log.info("No refresh token found");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");
        }
        String refreshToken = optionalCookie.get().getValue();*/

        Optional<AuthTokenDTO> authTokenDTO = authService.refresh(refreshToken);
        if (authTokenDTO.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        /*Cookie accessTokenCookie = new Cookie("accessToken", authTokenDTO.get().getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        httpResponse.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", authTokenDTO.get().getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        httpResponse.addCookie(refreshTokenCookie);*/

        return ResponseEntity.ok(authTokenDTO.get());
    }

    private Optional<Cookie> getAccessTokenCookie(Cookie[] cookies) {
        if(cookies==null) {
            return Optional.empty();
        }
        Arrays.stream(cookies).forEach(cookie->log.info(cookie.getName()));
        return Arrays.stream(cookies).filter(cookie->cookie.getName().equalsIgnoreCase("accessToken")).findFirst();
    }

    private Optional<Cookie> getRefreshTokenCookie(Cookie[] cookies) {
        if(cookies==null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie->cookie.getName().equalsIgnoreCase("refreshToken")).findFirst();
    }
}
