package com.jake.restaurantservice.controller;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.servicetype.dto.ServiceTypeDTO;
import com.jake.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class RestaurantController {
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

    @GetMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> addMyRestaurant(@RequestParam Long businessId) {
        log.info("GET /restaurant <{}>", businessId);
        return ResponseEntity.ok(service.findRestaurantByBusinessId(businessId));
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



/*    private long getBusinessId(Authentication authentication) {
        return Long.valueOf(((Jwt) authentication.getPrincipal()).getClaimAsString("businessId"));
    }*/
}
