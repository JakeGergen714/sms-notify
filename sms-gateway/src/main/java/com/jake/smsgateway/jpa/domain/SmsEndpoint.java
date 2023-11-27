package com.jake.smsgateway.jpa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity(name = "sms_endpoint")
@Data
public class SmsEndpoint {
    @Id
    private BigInteger id;
    private String url;
    private long textsSent;
    private LocalDateTime lastResetDate;
}
