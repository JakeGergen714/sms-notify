package com.jake.restaurantservice.service;

import com.jake.datacorelib.floormap.jpa.FloorMap;
import com.jake.datacorelib.floormap.jpa.FloorMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FloorMapService {
   private final FloorMapRepository floorMapRepository;

    public FloorMap findFloorMapById(Long floorMapId) {
        return floorMapRepository.findById(floorMapId).orElseThrow();
    }
}
