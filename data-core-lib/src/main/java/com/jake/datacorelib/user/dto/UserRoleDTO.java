package com.jake.datacorelib.user.dto;

import com.jake.datacorelib.user.jpa.RoleType;
import lombok.Data;

@Data
public class UserRoleDTO {
    private Long userRoleId;
    private Long userId;
    private Long restaurantId;
    private RoleType roleType;
}
