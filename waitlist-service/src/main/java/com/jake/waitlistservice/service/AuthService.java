package com.jake.waitlistservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;

    public boolean validateJwt(String jwt) {
        RequestEntity<Void> requestEntity = RequestEntity.post("http://localhost:8081/validate").header("accessToken", jwt).build();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(requestEntity, Void.class);
       if(!responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
           return false;
       }
       return true;
    }
}
