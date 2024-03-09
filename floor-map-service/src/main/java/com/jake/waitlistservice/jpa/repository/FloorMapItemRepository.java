package com.jake.waitlistservice.jpa.repository;

import com.jake.waitlistservice.jpa.domain.FloorMapItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface FloorMapItemRepository extends JpaRepository<FloorMapItem, BigInteger> {
    List<FloorMapItem> findAllByFloorMapId(BigInteger floorMapId, Sort sortedBy);

    List<FloorMapItem> findAllByFloorMapId(BigInteger floorMapId);
}
