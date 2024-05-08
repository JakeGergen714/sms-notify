package com.jake.reservationservice.service;

import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * Service class for retrieving restaurant information from the Restaurant Service.
 */
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public Restaurant findRestaurantByRestaurantId(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow();
    }
}
