package com.jake.datacorelib.servicetype.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Provider;
import java.util.List;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

    List<Provider.Service> findAllByRestaurantId(long restaurantId);
}
