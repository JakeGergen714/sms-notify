package com.jake.auth.controller;

import com.jake.auth.dto.UserCredentialDTO;
import com.jake.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@RestController
@Log4j2
public class Controller {
    private final AuthService authService;

    @PostMapping(value ="/signup")
    public ResponseEntity<String> signUp(@RequestBody UserCredentialDTO userCredentialDTO) throws UnsupportedEncodingException {
        log.info("/signup <{}>", userCredentialDTO);

        if(!authService.isUserNameAvailable(userCredentialDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A user with that username already exists.");
        }

        if(!authService.signUp(userCredentialDTO)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sign up failed.");
        }
        return ResponseEntity.ok().build();
    }
}
