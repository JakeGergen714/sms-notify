package com.jake.datacorelib.servicetype.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jake.datacorelib.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.serviceschedule.dto.ServiceScheduleDTO;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class ServiceTypeDTO {
    private Long serviceTypeId;
    private Long restaurantId;
    private String name;
    private Set<ServiceScheduleDTO> serviceSchedules;
    private FloorMap floorMap;
}
