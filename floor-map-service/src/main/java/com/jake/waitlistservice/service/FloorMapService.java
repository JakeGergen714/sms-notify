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

@RequiredArgsConstructor
@Component
public class FloorMapService {
    private final FloorMapItemRepository repo;
    private final FloorMapRepository floorMapRepository;

    public List<FloorMap> findAllForBusinessId(BigInteger businessId) {
        return floorMapRepository.findAllByBusinessId(businessId);
    }

    public FloorMap save(FloorMapDTO floorMapDTO, BigInteger businessId) {
        FloorMap floorMap = new FloorMap();
        floorMap.setName(floorMapDTO.getName());
        floorMap.setBusinessId(businessId);
        return floorMapRepository.save(floorMap);
    }


    public List<FloorMapItem> findAllForUser(BigInteger floorMapId) {
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
