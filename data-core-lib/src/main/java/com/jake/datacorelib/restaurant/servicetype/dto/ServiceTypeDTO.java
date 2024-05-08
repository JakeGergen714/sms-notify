package com.jake.datacorelib.restaurant.servicetype.dto;

import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.servicetype.serviceschedule.dto.ServiceScheduleDTO;
import lombok.Data;

import java.util.Set;

@Data
public class ServiceTypeDTO {
    private Long serviceTypeId;
    private Long restaurantId;
    private String name;
    private Set<ServiceScheduleDTO> serviceSchedules;
    private FloorMapDTO floorMap;
}
