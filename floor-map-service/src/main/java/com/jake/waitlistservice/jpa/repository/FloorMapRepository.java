package com.jake.waitlistservice.jpa.repository;

import com.jake.waitlistservice.jpa.domain.FloorMap;
import com.jake.waitlistservice.jpa.domain.FloorMapItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface FloorMapRepository extends JpaRepository<FloorMap, BigInteger> {
    List<FloorMap> findAllByBusinessId(long businessId, Sort sortedBy);

    List<FloorMap> findAllByBusinessId(long businessId);

    Optional<FloorMap> findByName(String name);
}
