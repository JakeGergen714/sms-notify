package com.jake.datacorelib.guest.dto;

import com.jake.datacorelib.guest.jpa.GuestStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class GuestDTO {
    private Long id;
    private String name;
    private String phone;
    @Enumerated(EnumType.ORDINAL)
    private GuestStatus status;
}
