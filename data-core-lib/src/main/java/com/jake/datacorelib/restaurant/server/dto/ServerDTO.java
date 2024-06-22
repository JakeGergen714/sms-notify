package com.jake.datacorelib.restaurant.server.dto;

import com.jake.datacorelib.restaurant.servicetype.serviceschedule.jpa.ServiceSchedule;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;

@Data
public class ServerDTO {
    private Long serverId;
    private String name;
    private Long restaurantId;
    private List<Long> serviceScheduleIds;
}
