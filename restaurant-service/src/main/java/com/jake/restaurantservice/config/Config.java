package com.jake.restaurantservice.config;


import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapItem;
import com.jake.datacorelib.restaurant.server.dto.ServerDTO;
import com.jake.datacorelib.restaurant.server.jpa.Server;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("*")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH").allowCredentials(true);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(ServiceType.class, ServiceTypeDTO.class)
                   .addMapping(src -> src.getRestaurant().getRestaurantId(), ServiceTypeDTO::setRestaurantId);


        modelMapper.createTypeMap(FloorMap.class, FloorMapDTO.class)
                   .addMapping(src -> src.getRestaurant().getRestaurantId(), FloorMapDTO::setRestaurantId);

        modelMapper.createTypeMap(FloorMapItem.class, FloorMapItemDTO.class)
                   .addMapping(src -> src.getFloorMap().getFloorMapId(), FloorMapItemDTO::setFloorMapId);

        modelMapper.createTypeMap(Server.class, ServerDTO.class)
                   .addMapping(src -> src.getRestaurant().getRestaurantId(), ServerDTO::setRestaurantId);


        return modelMapper;
    }
}