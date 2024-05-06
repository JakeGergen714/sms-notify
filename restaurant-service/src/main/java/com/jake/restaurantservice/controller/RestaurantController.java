package com.jake.restaurantservice.controller;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.servicetype.dto.ServiceTypeDTO;
import com.jake.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

   @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> getMyRestaurant(Authentication authenticationToken) {
       log.info("GET /restaurant");
       RestaurantDTO restaurant = service.findRestaurantByBusinessId(getBusinessId(authenticationToken));
       log.info("GET /restaurant <{}>", restaurant);
       return ResponseEntity.ok(restaurant);
    }

    @PostMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> addMyRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("POST /restaurant <{}>", restaurantDTO);
        RestaurantDTO savedDTO = service.addRestaurant(restaurantDTO, 1l);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping(value = "/servicetype")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@RequestBody ServiceTypeDTO serviceType) {
        log.info("PUT /servicetype <{}>", serviceType);
        ServiceTypeDTO savedDTO = service.updateServiceType(serviceType);
        log.info("Updated DTO <{}>",savedDTO );
        return ResponseEntity.ok(savedDTO);
    }
    
    private long getBusinessId(Authentication authentication) {
        return Long.valueOf(((Jwt) authentication.getPrincipal()).getClaimAsString("businessId"));
    }
}
