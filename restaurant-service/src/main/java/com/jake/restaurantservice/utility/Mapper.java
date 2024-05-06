package com.jake.restaurantservice.utility;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Log4j2
public class Mapper {
    private final ModelMapper modelMapper;

    public Restaurant toEntity(RestaurantDTO dto) {
        Restaurant restaurant = modelMapper.map(dto, Restaurant.class);
        restaurant.getServiceTypes().forEach(serviceType -> {
            serviceType.setRestaurant(restaurant);
            serviceType.getServiceSchedules().forEach(serviceSchedule -> {
                serviceSchedule.setServiceType(serviceType);
            });
        });

        return restaurant;
    }

    public RestaurantDTO toDTO(Restaurant entity) {
        RestaurantDTO dto = modelMapper.map(entity, RestaurantDTO.class);

        return dto;
    }
}
