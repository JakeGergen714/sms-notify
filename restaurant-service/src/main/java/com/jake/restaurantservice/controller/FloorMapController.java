package com.jake.restaurantservice.controller;

import com.jake.datacorelib.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.floormap.jpa.FloorMap;
import com.jake.datacorelib.floormap.jpa.FloorMapItem;
import com.jake.restaurantservice.service.FloorMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class FloorMapController {
    private final FloorMapService service;

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @GetMapping(value = "/floorMaps")
    public ResponseEntity<List<FloorMapDTO>> getAllFloorMaps(Authentication authenticationToken) {
        log.info("GET /floorMap");

        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        log.info(jwt.getClaims());
        long businessId =Long.valueOf(jwt.getClaimAsString("businessId"));

        List<FloorMapDTO> items = service.findAllForBusinessId(businessId);
        log.info("Found Items <{}>", items);
        return ResponseEntity.ok(items);
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @GetMapping(value = "/floorMap")
    public ResponseEntity<FloorMap> getFloorMapById(Authentication authenticationToken, @RequestParam Long floorMapId) {
        log.info("GET /floorMap");

        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        log.info(jwt.getClaims());
        long businessId =Long.valueOf(jwt.getClaimAsString("businessId"));

        return ResponseEntity.ok(service.findFloorMapById(floorMapId).orElseThrow());
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PostMapping(value = "/floorMap")
    public ResponseEntity<FloorMap> addFloorMap(Authentication authenticationToken, @RequestBody FloorMapDTO floorMapDTO) {
        log.info("POST /floorMap <{}>",floorMapDTO);

        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        log.info(jwt.getClaims());
        log.info(jwt.getSubject());
        log.info(jwt.getHeaders());
        log.info(jwt.getAudience());
        long businessId = Long.valueOf(jwt.getClaimAsString("businessId"));

        return ResponseEntity.ok(service.save(floorMapDTO, businessId));
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PutMapping(value = "/floorMap")
    public ResponseEntity<FloorMap> editFloorMap(Authentication authenticationToken, @RequestBody FloorMapDTO floorMapDTO) {
        log.info("PUT /floorMap <{}>",floorMapDTO);

        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        long businessId = Long.valueOf(jwt.getClaimAsString("businessId"));

        return ResponseEntity.ok(service.save(floorMapDTO, businessId));
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PostMapping(value = "/floorMapItem")
    public FloorMapItem save(Authentication authenticationToken, @RequestBody FloorMapItemDTO floorMapItemDTO) {
        log.info("POST /floorMapItem <{}>", floorMapItemDTO);
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();

        log.info("Adding floor map item item for username <{}>", username);
        return service.save(floorMapItemDTO);
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PutMapping(value = "/floorMapItem")
    public ResponseEntity<HttpStatus> updateFloorPlanItem(Authentication authenticationToken, @RequestBody FloorMapItemDTO floorMapItemDTO) {
        log.info("PUT /floorMapItem <{}>", floorMapItemDTO);
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();

        log.info("Adding floor map item item for username <{}>", username);
        service.update(floorMapItemDTO);

        return ResponseEntity.ok().build();
    }
}
