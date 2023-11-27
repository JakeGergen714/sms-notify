package com.jake.smsgateway.jpa.domain;

import com.jake.smsgateway.dto.ProcessedIndicator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigInteger;
import java.time.Instant;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "sms_request")
@Data
public class SmsRequest {

    @Id
    private BigInteger requestId;
    private String toNumber;
    private String textMessage;
    private String processedInd;
    @CreationTimestamp
    private Instant createDate;
    @UpdateTimestamp
    private Instant lastUpdateDate;

}
