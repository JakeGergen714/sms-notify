package com.jake.datacorelib.floormap.jpa;

import com.jake.datacorelib.jpa.domain.FloorMapItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorMapItemRepository extends JpaRepository<FloorMapItem, Long> {
    List<FloorMapItem> findAllByFloorMapId(long floorMapId, Sort sortedBy);

    List<FloorMapItem> findAllByFloorMapId(long floorMapId);
}
