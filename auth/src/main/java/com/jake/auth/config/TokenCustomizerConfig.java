package com.jake.auth.config;

import com.jake.auth.userdetails.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
@Log4j2
public class TokenCustomizerConfig {
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                UserDetailsImpl userDetails = (UserDetailsImpl) context.getPrincipal().getPrincipal();
                context.getClaims().claims((claims) -> {
                    if(userDetails.getUser().getBusiness() != null) {
                        claims.put("businessId", userDetails.getUser().getBusiness().getBusinessId());
                        claims.put("Roles", userDetails.getRoles());
                    }
                });
                log.info(context.getClaims());
            }
        };
    }

}
