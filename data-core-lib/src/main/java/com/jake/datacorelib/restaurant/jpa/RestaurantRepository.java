package com.jake.datacorelib.restaurant.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByBusinessId(Long businessId);
}
