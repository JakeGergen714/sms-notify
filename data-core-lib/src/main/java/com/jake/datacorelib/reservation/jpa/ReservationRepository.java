package com.jake.datacorelib.reservation.jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByRestaurantId(long restaurantId, Sort sortedBy);

    List<Reservation> findAllByRestaurantIdAndReservationDate(long restaurantId, LocalDate reservationDate, Sort sortedBy);

    List<Reservation> findAllByRestaurantIdAndReservationDateBetween(long restaurantId, LocalDate reservationDateStart, LocalDate reservationDateEnd, Sort sortedBy);
}
