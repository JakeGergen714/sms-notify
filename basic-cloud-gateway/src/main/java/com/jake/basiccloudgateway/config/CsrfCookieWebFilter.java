package com.jake.basiccloudgateway.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Log4j2
public class CsrfCookieWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String key = CsrfToken.class.getName();

        Mono<CsrfToken> csrfToken = exchange.getAttribute(key);
        if (csrfToken != null) {
            log.info("ADDING COOKIE");
            return csrfToken.doOnSuccess(token -> {
                ResponseCookie cookie = ResponseCookie.from("XSRF-TOKEN", token.getToken())
                        .maxAge(Duration.ofHours(1)).httpOnly(false).path("/").sameSite(Cookie.SameSite.LAX.attributeValue()).build();
                exchange.getResponse().getCookies().add("XSRF-TOKEN", cookie);
            }).then(chain.filter(exchange));
        } else {
            log.info("NOT ADDING COOKIE");
            return Mono.empty();
        }
    }
}
