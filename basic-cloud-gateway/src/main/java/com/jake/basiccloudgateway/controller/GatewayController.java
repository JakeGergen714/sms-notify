package com.jake.basiccloudgateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class GatewayController {

    private final ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager;

    private final ReactiveOAuth2AuthorizedClientService clientService;
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    private final WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.gateway.client-id}")
    private String clientId;


    /**
     * Manually refreshes the OAuth2 access token for a given client using the refresh token.
     * <p>
     * This method handles the complete flow of refreshing an OAuth2 access token. It first loads
     * the authorized client based on the registration ID and the authenticated principal name.
     * If a valid client and refresh token are found, it sends a refresh token request to the
     * authorization server. Upon successful refresh, it updates the OAuth2AuthorizedClient with
     * the new access token and refresh token and saves this updated client.
     * <p>
     * This endpoint was created so that if a service updates the roles for a given Authenticated User
     * then they could use this endpoint to refresh the token associated with that client which is stored
     * in this spring cloud gateways memory. This ensures a smooth user experience for all users
     * accessing services through this gateway so that their user roles are always up-to-date.
     *
     * @param authentication The current authentication object containing the principal's details.
     * @return A {@link Mono<ResponseEntity>} that completes when the token refresh process is finished. If the process
     * fails, the error is logged and the Mono completes with an error.
     */
    @GetMapping("/session/refresh-token")
    public Mono<? extends ResponseEntity<?>> manualTokenRefresh(Authentication authentication) {
        Logger log = LoggerFactory.getLogger(this.getClass());

        String registrationId = clientId;
        String principalName = authentication.getName();

        // Load the current authorized client
        return clientService.loadAuthorizedClient(registrationId, principalName).flatMap(client -> {
            if (client == null || client.getRefreshToken() == null) {
                log.info("No valid client or refresh token available.");
                return Mono.just(ResponseEntity.badRequest().build());
            }

            // Get the client registration
            return clientRegistrationRepository
                    .findByRegistrationId(registrationId)
                    .flatMap(clientRegistration -> sendRefreshTokenRequest(clientRegistration, client.getRefreshToken()))
                    .flatMap(requestResponse -> {
                        String accessTokenValue = (String) requestResponse.get("access_token");
                        Instant expiresAt = Instant.now().plusSeconds(((Number) requestResponse.get("expires_in")).longValue());
                        OAuth2AccessToken newAccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessTokenValue, Instant.now(), expiresAt);
                        OAuth2RefreshToken newRefreshTokenToken = new OAuth2RefreshToken((String) requestResponse.get("refresh_token"), Instant.now());
                        OAuth2AuthorizedClient updatedClient = new OAuth2AuthorizedClient(
                                client.getClientRegistration(),
                                client.getPrincipalName(),
                                newAccessToken,
                                newRefreshTokenToken);
                        // First, remove the old client
                        return clientService.removeAuthorizedClient(registrationId, "test2")
                                            .doOnSuccess(c -> log.info("removed"))
                                            .doOnError(error -> log.error("Failed to remove the client: {}", error))
                                            .then(Mono.defer(() -> clientService.saveAuthorizedClient(updatedClient, authentication)
                                                                                .then(Mono.just(ResponseEntity.noContent().build()))));
                    });
        }).onErrorResume(error -> {
            log.error("Failed to refresh the token: ", error);
            return Mono.just(ResponseEntity.status(500).build());
        });
    }

    private Mono<Map> sendRefreshTokenRequest(ClientRegistration clientRegistration, OAuth2RefreshToken refreshToken) {
        // Encode client_id and client_secret for Basic Authorization
        String clientId = clientRegistration.getClientId();
        String clientSecret = clientRegistration.getClientSecret();
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Prepare the body as application/x-www-form-urlencoded
        String body = "grant_type=refresh_token&refresh_token=" + refreshToken.getTokenValue();

        return webClient.post()
                        .uri(clientRegistration.getProviderDetails().getTokenUri())
                        .header("Authorization", "Basic " + encodedCredentials)
                        .header("Content-Type", "application/x-www-form-urlencoded").bodyValue(body).retrieve().bodyToMono(Map.class);
    }

}
