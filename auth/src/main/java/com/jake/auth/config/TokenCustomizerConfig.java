package com.jake.auth.config;

import com.jake.auth.service.JpaUserDetailsService;
import com.jake.auth.userdetails.UserDetailsImpl;
import com.jake.datacorelib.user.dto.UserDTO;
import com.jake.datacorelib.user.dto.UserRoleDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.stream.Collectors;

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
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(userDetails.getUser().getUserId());
                userDTO.setUsername(userDetails.getUsername());
                if(userDetails.getUser().getBusiness() != null) {
                    userDTO.setBusinessId(userDetails.getUser().getBusiness().getBusinessId());
                }

                if(userDetails.getUser().getRoles() != null) {
                    userDTO.setUserRoles( userDetails.getUser().getRoles().stream().map(userRole -> {
                        UserRoleDTO dto = new UserRoleDTO();
                        dto.setUserId(userRole.getUser().getUserId());
                        dto.setRoleType(userRole.getRoleType());
                        dto.setUserRoleId(userRole.getUserRoleId());
                        log.info(userRole);
                        dto.setRestaurantId( userRole.getRestaurant().getRestaurantId());
                        return dto;
                    }).collect(Collectors.toSet()) );
                }
                claims.put("user", userDTO);
            });
            log.info(context.getClaims());

        };
    }

}
