package com.jake.waitlistservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${url.auth-service}")
    private String authBaseUrl;

    public boolean validateJwt(String jwt) {
        RequestEntity<Void> requestEntity = RequestEntity.post(authBaseUrl + "/validate").header("accessToken", jwt).build();
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(requestEntity, Void.class);
            if (!responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to validate jwt.", e);
            return false;
        }
        return true;
    }
}
