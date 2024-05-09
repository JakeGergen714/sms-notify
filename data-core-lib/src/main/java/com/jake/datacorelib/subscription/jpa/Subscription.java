package com.jake.datacorelib.subscription.jpa;

import com.jake.datacorelib.business.jpa.Business;
import com.jake.datacorelib.subscription.SubscriptionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @OneToOne
    @JoinColumn(name="business_id", nullable = false)
    private Business business;

    @Enumerated
    private SubscriptionType subscriptionType;

    private Instant subscriptionStartInstant;

    private Instant subscriptionEndInstant;

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant updateDate;
}
