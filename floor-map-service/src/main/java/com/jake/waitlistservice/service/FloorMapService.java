package com.jake.waitlistservice.service;

import com.jake.waitlistservice.dto.FloorMapDTO;
import com.jake.waitlistservice.dto.FloorMapItemDTO;
import com.jake.waitlistservice.jpa.domain.FloorMap;
import com.jake.waitlistservice.jpa.domain.FloorMapItem;
import com.jake.waitlistservice.jpa.repository.FloorMapItemRepository;
import com.jake.waitlistservice.jpa.repository.FloorMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FloorMapService {
    private final FloorMapItemRepository repo;
    private final FloorMapRepository floorMapRepository;

    public List<FloorMap> findAllForBusinessId(long businessId) {
        return floorMapRepository.findAllByBusinessId(businessId);
    }

    public FloorMap save(FloorMapDTO floorMapDTO, long businessId) {
        Optional<FloorMap> floorMapOptional = floorMapRepository.findByName(floorMapDTO.getName());
        FloorMap floorMap;
        if(floorMapOptional.isEmpty()) {
            floorMap = new FloorMap();
            floorMap.setName(floorMapDTO.getName());
            floorMap.setBusinessId(businessId);
            FloorMap saved =  floorMapRepository.save(floorMap);
        } else {
            FloorMap existing = floorMapOptional.get();
            existing.setName(floorMapDTO.getName());
            floorMap = floorMapRepository.save(existing);
        }

        return floorMap;
    }


    public List<FloorMapItem> findAllForUser(long floorMapId) {
        return repo.findAllByFloorMapId(floorMapId);
    }

    public void add(FloorMapItemDTO floorMapItemDTO) {
        FloorMapItem floorMapItem = new FloorMapItem();

        floorMapItem.setFloorMapId(floorMapItemDTO.getFloorMapId());
        floorMapItem.setName(floorMapItemDTO.getName());
        floorMapItem.setMinTableSize(floorMapItemDTO.getMinPartySize());
        floorMapItem.setMinTableSize(floorMapItemDTO.getMaxPartySize());
        floorMapItem.setReservable(floorMapItemDTO.isReservable());
        repo.save(floorMapItem);
    }
}
