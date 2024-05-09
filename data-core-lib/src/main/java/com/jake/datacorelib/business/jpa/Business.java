package com.jake.datacorelib.business.jpa;

import com.jake.datacorelib.subscription.SubscriptionType;
import com.jake.datacorelib.subscription.jpa.Subscription;
import com.jake.datacorelib.user.jpa.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessId;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Subscription subscription;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<User> users;
}
