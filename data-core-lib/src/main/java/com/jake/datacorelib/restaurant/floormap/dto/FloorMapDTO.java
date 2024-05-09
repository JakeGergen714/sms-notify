package com.jake.datacorelib.restaurant.floormap.dto;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import lombok.Data;

import java.security.Provider;
import java.util.List;

@Data
public class FloorMapDTO {
    private Long floorMapId;
    private Long restaurantId;
    private String name;
    private List<FloorMapItemDTO> floorMapItems;
}
