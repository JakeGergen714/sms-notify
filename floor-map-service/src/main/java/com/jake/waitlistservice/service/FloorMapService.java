package com.jake.waitlistservice.service;

import com.jake.datacorelib.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.floormap.jpa.FloorMap;
import com.jake.datacorelib.floormap.jpa.FloorMapItem;
import com.jake.datacorelib.floormap.jpa.FloorMapItemRepository;
import com.jake.datacorelib.floormap.jpa.FloorMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

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
        dto.setTableType(entity.getTableType());
        dto.setXPosition(entity.getXPosition());
        dto.setYPosition(entity.getYPosition());
        dto.setMaxPartySize(entity.getMaxTableSize());
        dto.setMinPartySize(entity.getMinTableSize());
        dto.setReservable(entity.isReservable());
        return dto;
    }

    private FloorMapItem toEntity(FloorMapItemDTO dto) {
        FloorMapItem entity = new FloorMapItem();
        entity.setId(dto.getId());
        entity.setFloorMapId(dto.getFloorMapId());
        entity.setName(dto.getName());
        entity.setTableType(dto.getTableType());
        entity.setXPosition(dto.getXPosition());
        entity.setYPosition(dto.getYPosition());
        entity.setMinTableSize(dto.getMinPartySize());
        entity.setMaxTableSize(dto.getMaxPartySize());
        entity.setReservable(dto.isReservable());
        return entity;
    }

    public Optional<FloorMap> findFloorMapById(long floorMapId) {
        return floorMapRepository.findById(floorMapId);
    }

    public List<FloorMapDTO> findAllForBusinessId(long businessId) {
        log.info("Searching for Floor Maps by businessId <{}>", businessId);
        List<FloorMap> floorMaps = floorMapRepository.findAllByBusinessId(businessId);
        log.info("Found floor maps <{}>", floorMaps);
        List<FloorMapDTO> floorMapDTOS = new ArrayList<>();
        for (FloorMap floorMap : floorMaps) {
            List<FloorMapItem> floorMapItems = findAllForMapId(floorMap.getFloorMapId());
            log.info("Found Floor Map Items <{}> for Floor map Id <{}>", floorMapItems,  floorMap.getFloorMapId());
            FloorMapDTO floorMapDTO = new FloorMapDTO();
            floorMapDTO.setId(floorMap.getFloorMapId());
            floorMapDTO.setBusinessId(businessId);
            floorMapDTO.setName(floorMap.getName());
            floorMapDTO.setFloorMapItems(floorMapItems.stream().map(this::toDto).toList());
            floorMapDTOS.add(floorMapDTO);
        }

        return floorMapDTOS;
    }

    public FloorMap save(FloorMapDTO floorMapDTO, long businessId) {
        Optional<FloorMap> floorMapOptional = Optional.empty();
        if(floorMapDTO.getId() != null) {
            floorMapOptional = floorMapRepository.findById(floorMapDTO.getId());
        }
        FloorMap floorMap;
        if(floorMapOptional.isEmpty()) {
            log.info("No existing floor plan found");
            floorMap = new FloorMap();
            floorMap.setName(floorMapDTO.getName());
            floorMap.setBusinessId(businessId);


            log.info("Saved Floor Plan <{}>.", floorMap);
            FloorMap saved =  floorMapRepository.save(floorMap);
        } else {
            FloorMap existing = floorMapOptional.get();
            log.info("Found existing Floor Plan <{}>.", existing);
            existing.setName(floorMapDTO.getName());

            log.info("Edited existing Floor Plan <{}>.", existing);
            floorMap = floorMapRepository.save(existing);
            log.info("Saved Floor Plan <{}>.", existing);
            repo.saveAll(floorMapDTO.getFloorMapItems().stream().map(this::toEntity).toList());
        }

        return floorMap;
    }


    public List<FloorMapItem> findAllForMapId(long floorMapId) {
        return repo.findAllByFloorMapId(floorMapId);
    }

    public FloorMapItem save(FloorMapItemDTO floorMapItemDTO) {
        FloorMapItem floorMapItem = new FloorMapItem();

        floorMapItem.setFloorMapId(floorMapItemDTO.getFloorMapId());
        String name = floorMapItemDTO.getName();
        if(name == null) {
            name = generateFloorMapItemName(floorMapItemDTO);
            log.info("Generated name <{}>", name);
        }
        floorMapItem.setName(name);
        floorMapItem.setTableType(floorMapItemDTO.getTableType());
        floorMapItem.setMinTableSize(floorMapItemDTO.getMinPartySize());
        floorMapItem.setMinTableSize(floorMapItemDTO.getMaxPartySize());
        floorMapItem.setReservable(floorMapItemDTO.isReservable());
        floorMapItem.setXPosition(floorMapItemDTO.getXPosition());
        floorMapItem.setYPosition(floorMapItemDTO.getYPosition());

        return repo.save(floorMapItem);
    }

    public void update(FloorMapItemDTO floorMapItemDTO) {
        Optional<FloorMapItem> existingItemOptional = repo.findById(floorMapItemDTO.getId());

        if(existingItemOptional.isEmpty()) {
            log.info("Update failed no existing Floor Map Item found with id <{}>", floorMapItemDTO.getId());
            return;
        }
        FloorMapItem existingItem = existingItemOptional.get();
        log.info("Found <{}>", existingItem);
        existingItem.setXPosition(floorMapItemDTO.getXPosition());
        existingItem.setYPosition(floorMapItemDTO.getYPosition());
        existingItem.setName(floorMapItemDTO.getName());
        existingItem.setReservable(floorMapItemDTO.isReservable());
        existingItem.setMinTableSize(floorMapItemDTO.getMinPartySize());
        existingItem.setMaxTableSize(floorMapItemDTO.getMaxPartySize());
        existingItem.setTableType(floorMapItemDTO.getTableType());

        log.info("Added <{}>", repo.save(existingItem));
    }

    private String generateFloorMapItemName(FloorMapItemDTO floorMapItemDTO) {
        int count = repo.findAllByFloorMapId(floorMapItemDTO.getFloorMapId()).size();
        return String.valueOf(count + 1);
    }
}
