package com.jake.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;

@Data
@Entity(name = "business_user")
public class BusinessUser {
    @Id
    private String username;
    private Long businessId;
    private Boolean isAdmin;
}
