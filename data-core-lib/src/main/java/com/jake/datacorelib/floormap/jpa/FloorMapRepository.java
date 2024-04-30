package com.jake.datacorelib.floormap.jpa;

import com.jake.datacorelib.jpa.domain.FloorMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorMapRepository extends JpaRepository<FloorMap, Long> {
    List<FloorMap> findAllByBusinessId(long businessId, Sort sortedBy);

    List<FloorMap> findAllByBusinessId(long businessId);
}
