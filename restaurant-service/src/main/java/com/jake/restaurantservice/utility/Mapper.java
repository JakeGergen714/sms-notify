package com.jake.restaurantservice.utility;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
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

        return restaurant;
    }

    public RestaurantDTO toDTO(Restaurant entity) {
        return modelMapper.map(entity, RestaurantDTO.class);
    }

    public ServiceType toEntity(ServiceTypeDTO serviceTypeDTO) {
      return modelMapper.map(serviceTypeDTO, ServiceType.class);
    }

    public ServiceTypeDTO toDto(ServiceType serviceType) {
        return modelMapper.map(serviceType, ServiceTypeDTO.class);
    }
}
