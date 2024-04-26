package com.jake.reservationservice.service;

import com.jake.reservationservice.dto.ReservationDTO;
import com.jake.reservationservice.exception.TableReservedException;
import com.jake.reservationservice.jpa.domain.FloorMap;
import com.jake.reservationservice.jpa.domain.FloorMapItem;
import com.jake.reservationservice.jpa.domain.Reservation;
import com.jake.reservationservice.jpa.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RequiredArgsConstructor
@Component
@Log4j2
public class ReservationService {
    private final ReservationRepository repo;
    private final FloorMapService floorMapService;

    public List<Reservation> findAllByBusinessId(long businessId) {
        return repo.findAllByBusinessId(businessId, Sort.by(Sort.Direction.ASC, "reservationTime"));
    }

    public Set<LocalTime> findAllAvailableReservationsForDate(long businessId, LocalDate requestedReservationDate, int partySize) {

        LocalTime serviceTime = LocalTime.of(9, 30);

        FloorMap activeFloorMapForTime = floorMapService.getActiveFloorMapForTime(businessId, serviceTime);
        log.info("Found active floor map <{}>", activeFloorMapForTime);
        List<FloorMapItem> tables = floorMapService.findAllForMapId(activeFloorMapForTime.getId()).stream().filter(table -> table.getMinTableSize() <= partySize && table.getMaxTableSize() >= partySize).toList();
        log.info("Found tables <{}>", tables);
        List<Reservation> allTodaysReservations = repo.findAllByBusinessId(businessId, Sort.unsorted()).stream().filter(reservation -> reservation.getReservationTime().toLocalDate().equals(requestedReservationDate)).toList();
        log.info("Found reservations <{}>", allTodaysReservations);
        //Current Reservations and their assigned table
        // Lets assume reservation blocks are 2 hours and long and the starting reservation time is in increments of 30 min. i.e 5:00, 5:30, 6:00 etc...
        //So for a given table, X, assuming service is from 5pm - 10pm, has 10 available reservation times. and a max for 2 available reservations. 5-7, 7-9
        //but if X was unreserved 10 reservations time should be returned.
        // so for each table calculate all available reservation times

        Set<LocalTime> availableReservationTimeSlots = new HashSet<>();
        for (FloorMapItem item : tables) {
            //find all reservations for table for this date.
            List<LocalTime> tableTimeSlots = createReservationTimeSlots(activeFloorMapForTime.getServiceTimeStart(), activeFloorMapForTime.getServiceTimeEnd(), Duration.ofMinutes(30));
            for (Reservation reservation : allTodaysReservations) {
                if (reservation.getId() == item.getId()) {
                    //1:00, 1:30, 2:00, 2:30, 3:00, 3:30, 4:00, 4:30, 5:00, 5:30 <--- time slots
                    //3:00 <-- existing reservation
                    //[ anytime+{reservation time length} <= 3 ]----- [  3pm + {reservation time length}  ] ==== valid unreserved time slots
                    tableTimeSlots = tableTimeSlots.stream().filter(timeSlot -> {
                        if (timeSlot.isBefore(reservation.getReservationTime().toLocalTime())) {
                            return timeSlot.plusHours(2).isBefore(reservation.getReservationTime().toLocalTime()) || timeSlot.plusHours(2).equals(reservation.getReservationTime().toLocalTime());
                        } else {
                            return timeSlot.isAfter(reservation.getReservationTime().toLocalTime().plusHours(2)) || timeSlot.equals(reservation.getReservationTime().toLocalTime().plusHours(2));
                        }
                    }).toList();
                }
            }
            availableReservationTimeSlots.addAll(tableTimeSlots);
        }
        log.info("Available Reservations <{}>", availableReservationTimeSlots);

        return availableReservationTimeSlots;
    }

    private List<LocalTime> createReservationTimeSlots(LocalTime serviceStartTime, LocalTime serviceEndTime, Duration timeSlotGap) {
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime previous = serviceStartTime;
        while (previous.isBefore(serviceEndTime)) {
            timeSlots.add(previous);
            previous.plus(timeSlotGap);
        }
        timeSlots.add(serviceEndTime);
        return timeSlots;
    }

    public void add(ReservationDTO reservationDTO, long businessId) {
        if (!isTableFreeAtTime(reservationDTO.getTableId(), reservationDTO.getReservationTime())) {
            throw new TableReservedException(String.format("Table <%s> is not free at <%s>", reservationDTO.getTableId(), reservationDTO.getReservationTime()));
        }

        Reservation reservation = new Reservation();
        reservation.setBusinessId(businessId);
        reservation.setTableId(reservationDTO.getTableId());
        reservation.setPartyName(reservationDTO.getPartyName());
        reservation.setPartySize(reservationDTO.getPartySize());
        reservation.setNotes(reservationDTO.getNotes());
        repo.save(reservation);
    }


    private boolean isTableFreeAtTime(long tableId, LocalDateTime reservationTime) {
        Optional<Reservation> optionalReservation = repo.findByTableId(tableId);
        if (optionalReservation.isEmpty()) {
            return true;
        }

        Reservation reservation = optionalReservation.get();

        //hard coded table reservation length of 2 hours
        //If reservation is 2 hours or more from now, then table is free now
        Duration timeBetweenExistingReservation = Duration.between(reservation.getReservationTime(), reservationTime).abs();
        return timeBetweenExistingReservation.compareTo(Duration.ofHours(2)) >= 0;
    }

    private boolean isReservationConflicting(Reservation reservation, LocalDateTime reservationTime) {
        Duration timeBetweenExistingReservation = Duration.between(reservation.getReservationTime(), reservationTime).abs();
        return timeBetweenExistingReservation.compareTo(Duration.ofHours(2)) < 0;
    }
}
