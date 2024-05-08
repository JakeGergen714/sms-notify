package com.jake.reservationservice.service;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import com.jake.reservationservice.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


/**
 * Service class for retrieving restaurant information from the Restaurant Service.
 */
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;

    @Value("${services.restaurant.url}")
    private String baseUrl;

    /**
     * Retrieves restaurant information by its business ID.
     *
     * @param businessId The business ID of the restaurant to retrieve.
     * @return The RestaurantDTO representing the retrieved restaurant.
     * @throws RestaurantNotFoundException If the restaurant with the specified business ID is not found or if there is an error during the retrieval process.
     */
    public RestaurantDTO findRestaurantByBusinessId(Long businessId) {
        String url = baseUrl + "/restaurant?businessId={businessId}";
        try {
            ResponseEntity<RestaurantDTO> response = restTemplate.getForEntity(url, RestaurantDTO.class, businessId);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RestaurantNotFoundException(String.format("Failed to retrieve restaurant for business ID <%s>", businessId));
            }
        } catch (RestClientException ex) {
            throw new RestaurantNotFoundException(String.format("Failed to reach restaurant service <%s>", url));
        }
    }

    public Restaurant findRestaurantByRestaurantId(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow();
    }
}
