package com.jake.frontendapi.service;

import com.jake.frontendapi.dto.AuthTokenDTO;
import com.jake.frontendapi.dto.UserCredentialDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;
    @Value("${url.auth-service}")
    private String authBaseUrl;

    public boolean validateJwt(String jwt) {
        RequestEntity<Void> requestEntity = RequestEntity.post(authBaseUrl + "/validate").header("accessToken", jwt).build();

        ResponseEntity<Void> responseEntity = restTemplate.exchange(requestEntity, Void.class);
        return responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK);
    }

    public Optional<AuthTokenDTO> signIn(UserCredentialDTO userCredentialDTO) {
        RequestEntity<UserCredentialDTO> requestEntity = RequestEntity.post(authBaseUrl + "/signin").body(userCredentialDTO);
        ResponseEntity<AuthTokenDTO> response = restTemplate.exchange(requestEntity, AuthTokenDTO.class);
        if(response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public Optional<AuthTokenDTO> signUp(UserCredentialDTO userCredentialDTO) {
        log.info("Sending request entity to <{}>", authBaseUrl + "/signup");
        RequestEntity<UserCredentialDTO> requestEntity = RequestEntity.post(authBaseUrl + "/signup").body(userCredentialDTO);
        ResponseEntity<AuthTokenDTO> response = restTemplate.exchange(requestEntity, AuthTokenDTO.class);
        if(response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public Optional<AuthTokenDTO> refresh(String refreshToken) {
        RequestEntity<Void> requestEntity = RequestEntity.post(authBaseUrl + "/refresh").header("refreshToken", refreshToken).build();
        ResponseEntity<AuthTokenDTO> response = restTemplate.exchange(requestEntity, AuthTokenDTO.class);
        if(response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }
}
