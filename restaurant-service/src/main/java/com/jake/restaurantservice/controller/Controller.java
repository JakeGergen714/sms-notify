package com.jake.restaurantservice.controller;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final RestaurantService service;

/*    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/restaurant")
    public ResponseEntity<Restaurant> getMyRestaurant(Authentication authenticationToken) {
       Optional<Restaurant> optionalRestaurant = service.findRestaurantByBusinessId(getBusinessId(authenticationToken));
       if(optionalRestaurant.isEmpty()) {
           return ResponseEntity.notFound().build();
       }

       return ResponseEntity.ok(optionalRestaurant.get());
    }*/

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @PostMapping(value = "/restaurant")
    public ResponseEntity<Restaurant> addMyRestaurant(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = service.addRestaurant(restaurantDTO, 1l);
        return ResponseEntity.ok(restaurant);
    }

/*    private long getBusinessId(Authentication authentication) {
        return Long.valueOf(((Jwt) authentication.getPrincipal()).getClaimAsString("businessId"));
    }*/
}
