package com.jake.basiccloudgateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;

@Log4j2
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;
    private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

    @Bean
    @Order(0)
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        ServerCsrfTokenRepository tokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
        // set the name of the attribute the CsrfToken will be populated on
        delegate.setCsrfRequestAttributeName("_csrf");
        // Use only the handle() method of XorCsrfTokenRequestAttributeHandler and the
        // default implementation of resolveCsrfTokenValue() from CsrfTokenRequestHandler
        CsrfTokenRequestHandler requestHandler = delegate::handle;

        http
                .authorizeExchange(exchange -> exchange.anyExchange().authenticated())
                .csrf((csrf) -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(requestHandler)
                )
                .oauth2Login(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults());


        return http.build();
    }
}