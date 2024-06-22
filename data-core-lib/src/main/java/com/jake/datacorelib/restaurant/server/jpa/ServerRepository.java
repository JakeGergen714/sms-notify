package com.jake.datacorelib.restaurant.server.jpa;

import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server, Long> {

    List<Server> findByRestaurantRestaurantId(Long restaurantId);
}
