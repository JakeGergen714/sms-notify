package com.jake.waitlistservice.service;

import com.jake.waitlistservice.dto.FloorMapDTO;
import com.jake.waitlistservice.dto.FloorMapItemDTO;
import com.jake.waitlistservice.jpa.domain.FloorMap;
import com.jake.waitlistservice.jpa.domain.FloorMapItem;
import com.jake.waitlistservice.jpa.repository.FloorMapItemRepository;
import com.jake.waitlistservice.jpa.repository.FloorMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Log4j2
public class FloorMapService {
    private final FloorMapItemRepository repo;
    private final FloorMapRepository floorMapRepository;

    private FloorMapItemDTO toDto (FloorMapItem entity) {
        FloorMapItemDTO dto = new FloorMapItemDTO();
        dto.setId(entity.getId());
        dto.setFloorMapId(entity.getFloorMapId());
        dto.setName(entity.getName());
        dto.setMaxPartySize(entity.getMaxTableSize());
        dto.setMinPartySize(entity.getMinTableSize());
        dto.setReservable(entity.isReservable());
        return dto;
    }

    public List<FloorMapDTO> findAllForBusinessId(long businessId) {
        List<FloorMap> floorMaps = floorMapRepository.findAllByBusinessId(businessId);
        List<FloorMapDTO> floorMapDTOS = new ArrayList<>();
        for (FloorMap floorMap : floorMaps) {
            List<FloorMapItem> floorMapItems = findAllForMapId(floorMap.getId());


            FloorMapDTO floorMapDTO = new FloorMapDTO();
            floorMapDTO.setId(floorMap.getId());
            floorMapDTO.setBusinessId(businessId);
            floorMapDTO.setName(floorMap.getName());
            floorMapDTO.setFloorMapItems(floorMapItems.stream().map(this::toDto).toList());
            floorMapDTOS.add(floorMapDTO);
        }

        return floorMapDTOS;

    }

    public FloorMap save(FloorMapDTO floorMapDTO, long businessId) {
        Optional<FloorMap> floorMapOptional = floorMapRepository.findById(floorMapDTO.getId());
        FloorMap floorMap;
        if(floorMapOptional.isEmpty()) {
            log.info("No existing floor plan found");
            floorMap = new FloorMap();
            floorMap.setName(floorMapDTO.getName());
            floorMap.setBusinessId(businessId);
            FloorMap saved =  floorMapRepository.save(floorMap);
        } else {
            FloorMap existing = floorMapOptional.get();
            log.info("Found existing Floor Plan <{}>.", existing);
            existing.setName(floorMapDTO.getName());
            log.info("Edited existing Floor Plan <{}>.", existing);
            floorMap = floorMapRepository.save(existing);
            log.info("Saved Floor Plan <{}>.", existing);
        }

        return floorMap;
    }


    public List<FloorMapItem> findAllForMapId(long floorMapId) {
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
