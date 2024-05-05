package com.jake.datacorelib.servicetype.dto;

import com.jake.datacorelib.serviceschedule.dto.ServiceScheduleDTO;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class ServiceTypeDTO {
    private Long id;
    private Long restaurantId;
    private String name;

    private Set<ServiceScheduleDTO> serviceSchedules;
}
