package com.jake.datacorelib.restaurant.floormap.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorMapItemRepository extends JpaRepository<FloorMapItem, Long> {

}
