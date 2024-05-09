package com.jake.datacorelib.user.dto;

import com.jake.datacorelib.user.jpa.UserRole;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long userId;
    private Long businessId;
    private String username;
    private String password;
    private Set<UserRole> userRoles;
}
