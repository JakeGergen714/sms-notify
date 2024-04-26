package com.jake.reservationservice.service;

import com.jake.reservationservice.exception.FloorMapNotFoundException;
import com.jake.reservationservice.jpa.domain.FloorMap;
import com.jake.reservationservice.jpa.domain.FloorMapItem;
import com.jake.reservationservice.jpa.repository.FloorMapItemRepository;
import com.jake.reservationservice.jpa.repository.FloorMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FloorMapService {
    private final FloorMapItemRepository floorMapItemRepo;
    private final FloorMapRepository floorMapRepository;

    public List<FloorMapItem> findAllForMapId(long floorMapId) {
        return floorMapItemRepo.findAllByFloorMapId(floorMapId);
    }

    public FloorMap getActiveFloorMapForTime(long businessId, LocalTime activeAtTime) {
        List<FloorMap> floorMaps = floorMapRepository.findAllByBusinessId(businessId);

        Optional<FloorMap> activeFloorMap = floorMaps.stream().filter(floorMap -> floorMap.getServiceTimeStart().isBefore(activeAtTime) && floorMap.getServiceTimeEnd().isAfter(activeAtTime)).findFirst();

        return activeFloorMap.orElseThrow(() -> new FloorMapNotFoundException(String.format("No Floor map found for time <%s>", activeAtTime)));
    }
}
