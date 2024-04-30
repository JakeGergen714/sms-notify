package com.jake.datacorelib.waitlist.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface WaitListItemRepository extends JpaRepository<WaitListItem, BigInteger> {
    List<WaitListItem> findAllByBusinessId(String businessId, Sort sortedBy);
}
