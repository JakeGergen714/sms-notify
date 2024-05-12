package com.jake.auth.config;

import com.jake.auth.service.JpaUserDetailsService;
import com.jake.auth.userdetails.UserDetailsImpl;
import com.jake.datacorelib.user.dto.UserRoleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

@Configuration
@Log4j2
public class TokenCustomizerConfig {
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(JpaUserDetailsService userDetailsService) {
        return (context) -> {
            log.info("here again");

            context.getClaims().claims((claims) -> {
                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(context.getPrincipal().getName());

                log.info(userDetails.getRoles());
                List<UserRoleDTO> roleDTOs = userDetails.getRoles().stream().map(role -> {
                    UserRoleDTO dto = new UserRoleDTO();
                    dto.setRoleType(role.getRoleType());
                    dto.setRestaurantId(role.getRestaurant().getRestaurantId());
                    dto.setUserId(role.getUser().getUserId());
                    return dto;
                }).toList();
                claims.put("Roles", roleDTOs);
            });
            log.info(context.getClaims());

        };
    }

}
