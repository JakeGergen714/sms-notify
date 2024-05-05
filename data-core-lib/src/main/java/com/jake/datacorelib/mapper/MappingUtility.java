package com.jake.datacorelib.mapper;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class MappingUtility{


    public static Restaurant toEntity(RestaurantDTO dto ) {
        return modelMapper.map(dto, Restaurant.class);
    }
}
