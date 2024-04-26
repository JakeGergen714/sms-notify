package com.jake.reservationservice.jpa.repository;

import com.jake.reservationservice.jpa.domain.FloorMapItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorMapItemRepository extends JpaRepository<FloorMapItem, Long> {
    List<FloorMapItem> findAllByFloorMapId(long floorMapId, Sort sortedBy);

    List<FloorMapItem> findAllByFloorMapId(long floorMapId);
}
