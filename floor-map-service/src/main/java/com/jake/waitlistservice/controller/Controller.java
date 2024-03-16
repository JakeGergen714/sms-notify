package com.jake.waitlistservice.controller;

import com.jake.waitlistservice.dto.FloorMapDTO;
import com.jake.waitlistservice.dto.FloorMapItemDTO;
import com.jake.waitlistservice.jpa.domain.FloorMap;
import com.jake.waitlistservice.jpa.domain.FloorMapItem;
import com.jake.waitlistservice.service.FloorMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
public class Controller {
    private final FloorMapService service;

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @GetMapping(value = "/floorMap")
    public ResponseEntity<List<FloorMap>> getAllFloorMaps(Authentication authenticationToken) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();
        log.info(jwt.getClaims());
        long businessId =Long.valueOf(jwt.getClaimAsString("businessId"));

        List<FloorMap> items = service.findAllForBusinessId(businessId);
        log.info("Found Items <{}>", items);
        return ResponseEntity.ok(items);
    }

    @CrossOrigin(origins = "http://192.168.1.241:8090", allowCredentials = "true")
    // Replace with your allowed origin
    @PostMapping(value = "/floorMap")
    public ResponseEntity<FloorMap> addFloorMap(Authentication authenticationToken, FloorMapDTO floorMapDTO) {
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
    @PostMapping(value = "/floorMapItem")
    public ResponseEntity<HttpStatus> save(Authentication authenticationToken, @RequestBody FloorMapItemDTO floorMapItemDTO) {
        Jwt jwt = (Jwt) authenticationToken.getPrincipal();
        String username = jwt.getSubject();

        log.info("Adding floor map item item for username <{}>", username);
        service.add(floorMapItemDTO);

        return ResponseEntity.ok().build();
    }
}
