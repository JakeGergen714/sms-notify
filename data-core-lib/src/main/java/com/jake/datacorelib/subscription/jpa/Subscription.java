package com.jake.datacorelib.subscription.jpa;

import com.jake.datacorelib.business.jpa.Business;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.subscription.SubscriptionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Data
@ToString(exclude = "restaurant")
@EqualsAndHashCode(exclude = "restaurant")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @OneToOne
    @JoinColumn(name="restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Enumerated
    private SubscriptionType subscriptionType;

    private Instant subscriptionStartInstant;

    private Instant subscriptionEndInstant;

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant updateDate;
}
