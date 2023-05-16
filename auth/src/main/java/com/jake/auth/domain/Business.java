package com.jake.auth.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity(name = "business")
public class Business {
    @Id
    @Column(name="id", nullable= false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_id_seq")
    @SequenceGenerator(name="business_id_seq", sequenceName="business_id_seq", allocationSize=1, initialValue = 10)
    private Long id;
    private String name;
}
