package com.jake.datacorelib.restaurant.dto;

import com.jake.datacorelib.servicetype.dto.ServiceTypeDTO;
import com.jake.datacorelib.servicetype.jpa.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class RestaurantDTO {

    private Long id;
    private String name;
    private String address;

    private Set<ServiceTypeDTO> serviceTypes;
}
