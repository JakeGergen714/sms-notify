package com.jake.restaurantservice.controller;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.restaurantservice.service.FloorMapService;
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
    private final RestaurantService restaurantService;
    private final FloorMapService floorMapService;

   @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> getMyRestaurant(Authentication authenticationToken) {
       log.info("GET /restaurant");
       RestaurantDTO restaurant = restaurantService.findRestaurantByBusinessId(getBusinessId(authenticationToken));
       log.info("GET /restaurant <{}>", restaurant);
       return ResponseEntity.ok(restaurant);
    }

    @PostMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> addMyRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        log.info("POST /restaurant <{}>", restaurantDTO);
        RestaurantDTO savedDTO = restaurantService.addRestaurant(restaurantDTO, 1l);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping(value = "/restaurant/servicetype")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@RequestBody ServiceTypeDTO serviceType) {
        log.info("PUT /servicetype <{}>", serviceType);
        ServiceTypeDTO savedDTO = restaurantService.updateServiceType(serviceType);
        log.info("Updated DTO <{}>",savedDTO );
        return ResponseEntity.ok(savedDTO);
    }

    @PostMapping(value = "/restaurant/floormap")
    public ResponseEntity<FloorMapDTO> addFloorMap(@RequestBody FloorMapDTO floorMapDTO) {
        log.info("POST /restaurant/floormap <{}>", floorMapDTO);
        return ResponseEntity.ok(floorMapService.addNewFloorMap(floorMapDTO));
    }

    @PostMapping(value = "/restaurant/floormap/table")
    public ResponseEntity<FloorMapItemDTO> addFloorMapTable(Authentication authentication, @RequestBody FloorMapItemDTO floorMapItemDTO) {
        log.info("POST /restaurant/floormap/table <{}>", floorMapItemDTO);
        return ResponseEntity.ok(floorMapService.addTableToFloorMap(floorMapItemDTO));
    }

    private long getBusinessId(Authentication authentication) {
        return Long.valueOf(((Jwt) authentication.getPrincipal()).getClaimAsString("businessId"));
    }
}
