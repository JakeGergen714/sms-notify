package com.jake.datacorelib.restaurant.dto;

import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import lombok.Data;

import java.util.Set;

@Data
public class RestaurantDTO {

    private Long restaurantId;
    private Long businessId;
    private String name;
    private String address;

    private Set<ServiceTypeDTO> serviceTypes;
    private Set<FloorMapDTO> floorMaps;
}
