package com.jake.datacorelib.business.dto;

import com.jake.datacorelib.user.jpa.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
public class BusinessDTO {
    private Long businessId;

    private String name;

    private Set<Long> userIds;
}
