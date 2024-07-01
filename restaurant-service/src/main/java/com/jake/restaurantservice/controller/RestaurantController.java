package com.jake.restaurantservice.controller;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.restaurant.server.dto.ServerDTO;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.restaurantservice.service.FloorMapService;
import com.jake.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final FloorMapService floorMapService;
    @Value("${services.gateway.uri}")
    private String gatewayUrl;

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/restaurants")
    public ResponseEntity<Set<RestaurantDTO>> getMyRestaurants(Authentication authenticationToken) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());

        log.info("GET /restaurants {}", getBusinessId(authenticationToken));
        Set<RestaurantDTO> restaurants = restaurantService.findRestaurantsByBusinessId(getBusinessId(authenticationToken));
        log.info("GET /restaurants <{}>", restaurants);
        return ResponseEntity.ok(restaurants);
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    @GetMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> getMyRestaurants(Authentication authenticationToken, @RequestParam Long restaurantId) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        log.info(jwt.getClaims());
        log.info("GET /restaurant {}", getBusinessId(authenticationToken));

        Set<Long> authorizedRestaurantIds = getRestaurantIds(authenticationToken);
        if (!authorizedRestaurantIds.contains(restaurantId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        return ResponseEntity.ok(restaurantService.findRestaurantById(restaurantId));
    }

    @PostMapping(value = "/restaurant")
    public ResponseEntity<RestaurantDTO> addMyRestaurant(Authentication authenticationToken, @RequestBody RestaurantDTO restaurantDTO) {
        log.info("POST /restaurant <{}>", restaurantDTO);
        RestaurantDTO savedDTO = restaurantService.addRestaurant(restaurantDTO, getUsername(authenticationToken));

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(gatewayUrl + "/session/refresh-token"));
        log.debug("User Roles have been updated. Redirecting to gateway to update access token user roles.");
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    @GetMapping(value = "/restaurant/servers")
    public ResponseEntity<List<ServerDTO>> getServers(Authentication authenticationToken, @RequestParam Long restaurantId) {
        log.info("Get /servers <{}>", restaurantId);
        List<ServerDTO> foundServers = restaurantService.getServers(restaurantId, getRestaurantIds(authenticationToken));
       log.info("Found servers <{}>", foundServers);
        return ResponseEntity.ok(foundServers);
    }

    @PostMapping(value = "/restaurant/server")
    public ResponseEntity<ServerDTO> addServer(Authentication authenticationToken, @RequestBody ServerDTO serverDTO) {
        log.info("POST /server <{}>", serverDTO);
        ServerDTO savedDTO = restaurantService.addServer(serverDTO, getRestaurantIds(authenticationToken));
        log.info("Added Server DTO <{}>", savedDTO);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping(value = "/restaurant/server")
    public ResponseEntity<ServerDTO> editServer(Authentication authenticationToken, @RequestBody ServerDTO serverDTO) {
        log.info("PUT /server <{}>", serverDTO);
        ServerDTO savedDTO = restaurantService.editServer(serverDTO, getRestaurantIds(authenticationToken));
        log.info("Updated Server DTO <{}>", savedDTO);
        return ResponseEntity.ok(savedDTO);
    }

    @PostMapping(value = "/restaurant/servicetype")
    public ResponseEntity<ServiceTypeDTO> addServiceType(Authentication authenticationToken, @RequestBody ServiceTypeDTO serviceType) {
        log.info("POST /servicetype <{}>", serviceType);
        ServiceTypeDTO savedDTO = restaurantService.addServiceType(serviceType, getRestaurantIds(authenticationToken));
        log.info("Updated DTO <{}>", savedDTO);
        return ResponseEntity.ok(savedDTO);
    }

    @PutMapping(value = "/restaurant/servicetype")
    public ResponseEntity<ServiceTypeDTO> updateServiceType(@RequestBody ServiceTypeDTO serviceType) {
        log.info("PUT /servicetype <{}>", serviceType);
        ServiceTypeDTO savedDTO = restaurantService.updateServiceType(serviceType);
        log.info("Updated DTO <{}>", savedDTO);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping(value = "/restaurant/floormaps")
    public ResponseEntity<Set<FloorMapDTO>> getAllFloorMapsByRestaurantId(Authentication authentication, @RequestParam Long restaurantId ) {
        Set<Long> authorizedIds = getRestaurantIds(authentication);
        if(!authorizedIds.contains(restaurantId)) {
            return ResponseEntity.ok(Set.of());
        }
        log.info("Get /restaurant/floormaps <{}>", restaurantId);
        return ResponseEntity.ok(floorMapService.findAllFloorMapsByRestaurantId(restaurantId));
    }

    @PostMapping(value = "/restaurant/floormap")
    public ResponseEntity<FloorMapDTO> addFloorMap(@RequestBody FloorMapDTO floorMapDTO) {
        log.info("POST /restaurant/floormap <{}>", floorMapDTO);
        return ResponseEntity.ok(floorMapService.addNewFloorMap(floorMapDTO));
    }

    @PutMapping(value = "/restaurant/floormap")
    public ResponseEntity<FloorMapDTO> updateFloorMap(Authentication authentication, @RequestBody FloorMapDTO floorMapDTO) {
        log.info("POST /restaurant/floormap <{}>", floorMapDTO);
        return ResponseEntity.ok(floorMapService.updateFloorMap(floorMapDTO, getRestaurantIds(authentication)));
    }

    @PostMapping(value = "/restaurant/floormap/table")
    public ResponseEntity<FloorMapItemDTO> addFloorMapTable(Authentication authentication, @RequestBody FloorMapItemDTO floorMapItemDTO) {
        log.info("POST /restaurant/floormap/table <{}>", floorMapItemDTO);
        return ResponseEntity.ok(floorMapService.addTableToFloorMap(floorMapItemDTO, getRestaurantIds(authentication)));
    }

    @PostMapping(value = "/restaurant/floormap/tables")
    public ResponseEntity<Set<FloorMapItemDTO>> addAllFloorMapTable(Authentication authentication, @RequestBody Set<FloorMapItemDTO> floorMapItemDTOs) {
        log.info("POST All /restaurant/floormap/table <{}>", floorMapItemDTOs);
        return ResponseEntity.ok(floorMapItemDTOs
                .stream()
                .map(dto -> floorMapService.addTableToFloorMap(dto, getRestaurantIds(authentication)))
                .collect(Collectors.toSet()));
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PutMapping(value = "/restaurant/floormap/table")
    public ResponseEntity<FloorMapItemDTO> updateFloorPlanItem(Authentication authenticationToken, @RequestBody FloorMapItemDTO floorMapItemDTO) {
        log.info("PUT /floorMapItem <{}>", floorMapItemDTO);

        return ResponseEntity.ok(floorMapService.updateTable(floorMapItemDTO, getRestaurantIds(authenticationToken)));
    }

    private String getUsername(Authentication authentication) {
        return ((Jwt) authentication.getPrincipal()).getSubject();
    }

    private Long getBusinessId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<Object, Object> user = jwt.getClaim("user");
        return (Long) user.get("businessId");
    }

    private Set<Long> getRestaurantIds(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<Object, Object> user = jwt.getClaim("user");
        List<Map<Object, Object>> userRoles = (List<Map<Object, Object>>) user.get("userRoles");

        return userRoles.stream().map(userRole -> (Long) userRole.get("restaurantId")).collect(Collectors.toSet());
    }
}
