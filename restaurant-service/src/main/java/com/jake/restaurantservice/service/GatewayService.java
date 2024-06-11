package com.jake.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Log4j2
public class GatewayService {
    private final RestTemplate restTemplate;



    /*public void userRolesUpdated(Authentication authenticationToken) {
        // Set up headers for RestTemplate call
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + ((Jwt) authenticationToken.getCredentials()).getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String sessionRefreshTokenEndpoint = gatewayUrl + "/session/refresh-token";
        // Call the refresh token endpoint
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                sessionRefreshTokenEndpoint,
                HttpMethod.GET,
                entity,
                Void.class
        );

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RefreshTokenException("Failed to refresh access token");
        } else {
            log.info("Successfully updated access token stored in gateway session storage.");
        }
    }*/
}
