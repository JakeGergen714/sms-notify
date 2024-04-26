package com.jake.reservationservice.jpa.repository;

import com.jake.reservationservice.jpa.domain.FloorMap;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FloorMapRepository extends JpaRepository<FloorMap, Long> {
    List<FloorMap> findAllByBusinessId(long businessId, Sort sortedBy);

    List<FloorMap> findAllByBusinessId(long businessId);
}
