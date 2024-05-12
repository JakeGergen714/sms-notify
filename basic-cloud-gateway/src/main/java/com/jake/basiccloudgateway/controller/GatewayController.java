package com.jake.basiccloudgateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/test")
    public Mono<String> manualTokenRefresh(Authentication authentication, ServerWebExchange exchange) {
        Logger log = LoggerFactory.getLogger(this.getClass());
        log.info("begin");

        String registrationId = "gateway";
        String principalName = authentication.getName();

        // Load the current authorized client
        return clientService.loadAuthorizedClient(registrationId, principalName).flatMap(client -> {
            if (client == null || client.getRefreshToken() == null) {
                log.info("No valid client or refresh token available.");
                return Mono.error(new IllegalStateException("No valid refresh token available."));
            }
            log.info("Current Access Token: {}", client.getAccessToken().getTokenValue());

            // Get the client registration
            return clientRegistrationRepository.findByRegistrationId(registrationId).flatMap(clientRegistration -> sendRefreshTokenRequest(clientRegistration, client.getRefreshToken(), log)).flatMap(newAccessToken -> {
                // First, remove the old client
                return clientService.removeAuthorizedClient("gateway", "test2").doOnSuccess(c -> log.info("removed")).doOnError(error -> log.error("Failed to remove the client: {}", error)).then(Mono.defer(() -> {
                    // Update the OAuth2AuthorizedClient with the new access token
                    OAuth2AuthorizedClient updatedClient = new OAuth2AuthorizedClient(client.getClientRegistration(), client.getPrincipalName(), newAccessToken, client.getRefreshToken());

                    // Save the updated authorized client
                    return clientService.saveAuthorizedClient(updatedClient, authentication).then(Mono.defer(() -> {
                        // Load the updated client to confirm the new token
                        return clientService.loadAuthorizedClient(registrationId, principalName).doOnSuccess(updated -> {
                            if (updated != null) {
                                log.info("New Access Token after save: {}", updated.getAccessToken().getTokenValue());
                            } else {
                                log.error("Failed to load updated client after save.");
                            }
                        }).thenReturn("New Access Token: " + newAccessToken.getTokenValue());
                    }));
                }));
            });
        }).onErrorResume(error -> {
            log.error("Failed to refresh the token: ", error);
            return Mono.just("error");
        });
    }

    private Mono<OAuth2AccessToken> sendRefreshTokenRequest(ClientRegistration clientRegistration, OAuth2RefreshToken refreshToken, Logger log) {
        // Encode client_id and client_secret for Basic Authorization
        String clientId = clientRegistration.getClientId();
        String clientSecret = clientRegistration.getClientSecret();
        String credentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Prepare the body as application/x-www-form-urlencoded
        String body = "grant_type=refresh_token&refresh_token=" + refreshToken.getTokenValue();

        return webClient.post().uri(clientRegistration.getProviderDetails().getTokenUri()).header("Authorization", "Basic " + encodedCredentials).header("Content-Type", "application/x-www-form-urlencoded").bodyValue(body).retrieve().bodyToMono(Map.class).flatMap(response -> {
            String accessTokenValue = (String) response.get("access_token");
            Instant expiresAt = Instant.now().plusSeconds(((Number) response.get("expires_in")).longValue());
            return Mono.just(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessTokenValue, Instant.now(), expiresAt));
        });
    }

}
