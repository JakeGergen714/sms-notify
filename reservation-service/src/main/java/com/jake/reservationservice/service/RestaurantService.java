package com.jake.reservationservice.service;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestTemplate restTemplate;

    @Value("${services.restaurant.url}")
    private String baseUrl;

    public RestaurantDTO findRestaurantByBusinessId(Long businessId) {
        return restTemplate.getForObject(baseUrl + "/restaurant", RestaurantDTO.class, Map.of("businessId", 1l));
    }
}
