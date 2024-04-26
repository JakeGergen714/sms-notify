package com.jake.reservationservice.jpa.repository;

import com.jake.reservationservice.jpa.domain.Reservation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, BigInteger> {
    List<Reservation> findAllByBusinessId(long businessId, Sort sortedBy);

    Optional<Reservation> findByTableId(long tableId);
}
