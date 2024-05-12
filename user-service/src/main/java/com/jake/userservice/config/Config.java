package com.jake.userservice.config;


import com.jake.datacorelib.user.dto.UserDTO;
import com.jake.datacorelib.user.dto.UserRoleDTO;
import com.jake.datacorelib.user.jpa.User;
import com.jake.datacorelib.user.jpa.UserRole;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        PropertyMap<User, UserDTO> userMap = new PropertyMap<User, UserDTO>() {
            protected void configure() {
                skip(destination.getPassword());
            }
        };

        // Add the property map to the ModelMapper
        modelMapper.addMappings(userMap)
                   .addMapping(src -> src.getBusiness().getBusinessId(), UserDTO::setBusinessId);
        modelMapper.createTypeMap(UserRole.class, UserRoleDTO.class)
                   .addMapping(src -> src.getUser().getUserId(), UserRoleDTO::setUserId);


        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}