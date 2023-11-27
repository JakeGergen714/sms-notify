package com.jake.waitlistservice.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jake.waitlistservice.dto.AuthorizationDTO;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import javax.management.relation.Role;

public class JwtUtil {

    public static AuthorizationDTO decodeJwt(String accessToken) {
        DecodedJWT decodedJwt = JWT.decode(accessToken);
        AuthorizationDTO dto = new AuthorizationDTO();
        dto.setUsername(decodedJwt.getSubject());
        dto.setRoles(decodedJwt.getClaims().values().stream().map(Claim::asString).toList());
        return dto;
    }

}
