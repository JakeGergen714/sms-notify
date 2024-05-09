package com.jake.datacorelib.subscription.dto;

import com.jake.datacorelib.business.jpa.Business;
import com.jake.datacorelib.subscription.SubscriptionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
public class SubscriptionDTO {

    private Long subscriptionId;

    private Long businessId;

    private SubscriptionType subscriptionType;

    private Instant subscriptionStartInstant;

    private Instant subscriptionEndInstant;
}
