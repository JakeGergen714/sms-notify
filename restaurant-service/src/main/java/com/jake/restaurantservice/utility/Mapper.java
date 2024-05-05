package com.jake.restaurantservice.utility;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Mapper {
    ModelMapper modelMapper;

    public Restaurant toEntity(RestaurantDTO dto ) {
        return modelMapper.map(dto, Restaurant.class);
    }
}
