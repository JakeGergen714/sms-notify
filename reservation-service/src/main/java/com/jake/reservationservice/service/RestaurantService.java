package com.jake.reservationservice.service;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestTemplate restTemplate;

    @Value("${services.restaurant.url}")
    private String baseUrl;

    public RestaurantDTO findRestaurantByBusinessId(Long businessId) {
        String url = baseUrl + "/restaurant?businessId={businessId}";
        return restTemplate.getForEntity(url, RestaurantDTO.class, 1l).getBody();
    }
}
