package com.jake.restaurantservice.service;

import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapItem;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapItemRepository;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapRepository;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import com.jake.restaurantservice.exception.FloorMapNotFoundException;
import com.jake.restaurantservice.exception.RestaurantNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Log4j2
public class FloorMapService {
    private final FloorMapRepository floorMapRepository;
    private final FloorMapItemRepository floorMapItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    public Optional<FloorMap> findFloorMapById(long floorMapId) {
        return floorMapRepository.findById(floorMapId);
    }

    @Transactional
    public FloorMapDTO addNewFloorMap(FloorMapDTO floorMapDTO) {
        FloorMap floorMap = modelMapper.map(floorMapDTO, FloorMap.class);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(floorMapDTO.getRestaurantId());
        if(optionalRestaurant.isEmpty()) {
            log.error("Restaurant with ID <{}> could not be found.", floorMapDTO.getRestaurantId());
            throw new RestaurantNotFoundException(floorMapDTO.getRestaurantId());
        }

        // Set the restaurant to the floorMap
        floorMap.setRestaurant(optionalRestaurant.get());

        log.info("Saving Floor Map <{}>", floorMap);
        FloorMap savedFloorMap = floorMapRepository.save(floorMap);
        log.info("Saved Floor Map <{}>", floorMap);
        return modelMapper.map(savedFloorMap, FloorMapDTO.class);
    }

    @Transactional
    public FloorMapItemDTO addTableToFloorMap(FloorMapItemDTO floorMapItemDTO) {
        FloorMapItem floorMapItem = modelMapper.map(floorMapItemDTO, FloorMapItem.class);
        floorMapItem.setFloorMapItemId(null);

        FloorMap floorMap;
        Optional<FloorMap> optionalFloorMap = floorMapRepository.findById(floorMapItemDTO.getFloorMapId());
        if(optionalFloorMap.isEmpty()) {
            log.error("Restaurant with ID <{}> could not be found.", floorMapItemDTO.getFloorMapId());
            throw new FloorMapNotFoundException(floorMapItemDTO.getFloorMapId());
        }

        // Set the restaurant to the floorMap
        floorMapItem.setFloorMap(optionalFloorMap.get());

        log.info("Saving Floor Map Item <{}>", floorMapItem);
        FloorMapItem savedFloorItemMap = floorMapItemRepository.save(floorMapItem);
        log.info("Saved Floor Map Item <{}>", floorMapItem);
        return modelMapper.map(savedFloorItemMap, FloorMapItemDTO.class);
    }
}
