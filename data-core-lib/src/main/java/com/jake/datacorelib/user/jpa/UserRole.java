package com.jake.datacorelib.user.jpa;

import com.jake.datacorelib.restaurant.jpa.Restaurant;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "restaurant_id"})})
@ToString(exclude = "user")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userRoleId;

    @Enumerated
    private RoleType roleType;

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        UserRole userRole = (UserRole) o;
        return userRoleId != null && Objects.equals(userRoleId, userRole.userRoleId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
