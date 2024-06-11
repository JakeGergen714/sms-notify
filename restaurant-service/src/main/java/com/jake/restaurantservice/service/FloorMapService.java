package com.jake.restaurantservice.service;

import com.jake.datacorelib.restaurant.floormap.dto.FloorMapDTO;
import com.jake.datacorelib.restaurant.floormap.dto.FloorMapItemDTO;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapItem;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapItemRepository;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapRepository;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import com.jake.restaurantservice.exception.FloorMapItemNotFoundException;
import com.jake.restaurantservice.exception.FloorMapNotFoundException;
import com.jake.restaurantservice.exception.ResourceForbiddenException;
import com.jake.restaurantservice.exception.RestaurantNotFoundException;
import com.jake.restaurantservice.utility.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Log4j2
public class FloorMapService {
    private final FloorMapRepository floorMapRepository;
    private final FloorMapItemRepository floorMapItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final Mapper mapper;


    public Optional<FloorMap> findFloorMapById(long floorMapId) {
        return floorMapRepository.findById(floorMapId);
    }

    public Set<FloorMapDTO> findAllFloorMapsByRestaurantId(long restaurantId) {
        return floorMapRepository.findByRestaurantRestaurantId(restaurantId).stream().map(floorMap -> modelMapper.map(floorMap, FloorMapDTO.class)).collect(Collectors.toSet());
    }

    @Transactional
    public FloorMapDTO addNewFloorMap(FloorMapDTO floorMapDTO) {
        FloorMap floorMap = modelMapper.map(floorMapDTO, FloorMap.class);

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(floorMapDTO.getRestaurantId());
        if (optionalRestaurant.isEmpty()) {
            log.error("Restaurant with ID <{}> could not be found.", floorMapDTO.getRestaurantId());
            throw new RestaurantNotFoundException(floorMapDTO.getRestaurantId());
        }
        Restaurant restaurant = optionalRestaurant.get();

        // Set the restaurant to the floorMap
        floorMap.setRestaurant(optionalRestaurant.get());

        log.info("Saving Floor Map <{}>", floorMap);
        FloorMap savedFloorMap = floorMapRepository.save(floorMap);
        log.info("Saved Floor Map <{}>", floorMap);


        return modelMapper.map(savedFloorMap, FloorMapDTO.class);
    }

    @Transactional
    public FloorMapDTO updateFloorMap(FloorMapDTO floorMapDTO, Set<Long> authorizedRestaurantIds) {
        FloorMap floorMap = floorMapRepository.findById(floorMapDTO.getFloorMapId())
                                              .orElseThrow(() -> new FloorMapNotFoundException(floorMapDTO.getFloorMapId()));

        if (!authorizedRestaurantIds.contains(floorMap.getRestaurant().getRestaurantId())) {
            throw new ResourceForbiddenException();
        }

        floorMap.setName(floorMapDTO.getName());

        return modelMapper.map(floorMapRepository.save(floorMap), FloorMapDTO.class);
    }


    @Transactional
    public FloorMapItemDTO addTableToFloorMap(FloorMapItemDTO floorMapItemDTO, Set<Long> authorizedIds) {
        FloorMapItem floorMapItem = modelMapper.map(floorMapItemDTO, FloorMapItem.class);
        floorMapItem.setFloorMapItemId(null);

        FloorMap floorMap = floorMapRepository
                .findById(floorMapItemDTO.getFloorMapId())
                .orElseThrow(() -> new FloorMapNotFoundException(floorMapItemDTO.getFloorMapId()));

        if (!authorizedIds.contains(floorMap.getRestaurant().getRestaurantId())) {
            throw new RestaurantNotFoundException(floorMap.getRestaurant().getRestaurantId());
        }

        floorMapItem.setChairCount(floorMapItemDTO.getChairCount());
        floorMapItem.setDisableChairsOnTop(floorMapItemDTO.isDisableChairsOnTop());
        floorMapItem.setDisableChairsOnBottom(floorMapItemDTO.isDisableChairsOnBottom());
        floorMapItem.setDisableChairsOnLeft(floorMapItemDTO.isDisableChairsOnLeft());
        floorMapItem.setDisableChairsOnRight(floorMapItemDTO.isDisableChairsOnRight());

        // Set the restaurant to the floorMap
        floorMapItem.setFloorMap(floorMap);

        log.info("Saving Floor Map Item <{}>", floorMapItem);
        FloorMapItem savedFloorItemMap = floorMapItemRepository.save(floorMapItem);
        log.info("Saved Floor Map Item <{}>", floorMapItem);
        return modelMapper.map(savedFloorItemMap, FloorMapItemDTO.class);
    }

    @Transactional
    public FloorMapItemDTO updateTable(FloorMapItemDTO floorMapItemDTO, Set<Long> authorizedRestaurantIds) {
        FloorMapItem table = floorMapItemRepository
                .findById(floorMapItemDTO.getFloorMapItemId())
                .orElseThrow(() -> new FloorMapItemNotFoundException(floorMapItemDTO.getFloorMapItemId()));

        if (!authorizedRestaurantIds.contains(table.getFloorMap().getRestaurant().getRestaurantId())) {
            throw new ResourceForbiddenException();
        }

        table.setTableType(floorMapItemDTO.getTableType());
        table.setName(floorMapItemDTO.getName());
        table.setMinTableSize(floorMapItemDTO.getMinPartySize());
        table.setMaxTableSize(floorMapItemDTO.getMaxPartySize());
        table.setReservable(floorMapItemDTO.isReservable());
        table.setXPosition(floorMapItemDTO.getXPosition());
        table.setYPosition(floorMapItemDTO.getYPosition());


        FloorMapItem savedFloorItemMap = floorMapItemRepository.save(table);
        log.info("Saved Floor Map Item <{}>", table);
        return modelMapper.map(savedFloorItemMap, FloorMapItemDTO.class);
    }
}
