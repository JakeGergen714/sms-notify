package com.jake.reservationservice.service;

import com.jake.datacorelib.reservation.jpa.Reservation;
import com.jake.datacorelib.reservation.jpa.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Log4j2
public class ReservationService {
    private final ReservationRepository repo;
    private final FloorMapService floorMapService;

    public List<Reservation> findAllByBusinessId(long businessId) {
        return repo.findAllByBusinessId(businessId, Sort.by(Sort.Direction.ASC, "reservationTime"));
    }

    //methods
    // 1. isReservationAvailableForTime(businessId, LocalTime reservationTime, int partySize)
    // 2. getAllAvailableReservationsTimesForDate(businessId, LocalDateTime, int partySize)

    // todo Floor plans need to have also, days of the week they are active, MON-FRI, or only
    // SAT-SUN
    // ... etc...
   /* public Set<LocalTime> findAllAvailableReservationsForDate(long businessId, LocalDate requestedReservationDate, int partySize) {

        //1. Retrieve all of the services
        //2. Retrieve the floor plan for that service


        LocalTime serviceStartTime = LocalTime.of(9, 30);
        LocalTime serviceEndTime = LocalTime.of(21, 30);

        FloorMap activeFloorMapForTime = floorMapService.getActiveFloorMapForTime(businessId, serviceTime);
        log.info("Found active floor map <{}>", activeFloorMapForTime);
        List<FloorMapItem> tables = floorMapService.findAllForMapId(activeFloorMapForTime.getId()).stream().filter(table -> table.getMinTableSize() <= partySize && table.getMaxTableSize() >= partySize).toList();
        log.info("Found tables <{}>", tables);
        List<Reservation> allTodaysReservations = repo.findAllByBusinessId(businessId, Sort.unsorted()).stream().filter(reservation -> reservation.getReservationTime().toLocalDate().equals(requestedReservationDate)).toList();
        log.info("Found reservations <{}>", allTodaysReservations);

        Set<LocalTime> timeSlots = createReservationTimeSlots(serviceStartTime, serviceEndTime, Duration.ofMinutes(30));
        tables.sort(Comparator.comparingInt(FloorMapItem::getMaxTableSize));

        Set<LocalTime> availableTimeSlots = new HashSet<>();
        for (LocalTime timeSlot : timeSlots) {
            List<Reservation> potentialConflicts = allTodaysReservations.stream().filter(reservation ->
                    !isTimeSlotAvailableGivenExistingReservationTime(timeSlot, reservation.getReservationTime().toLocalTime())).toList();

            potentialConflicts.sort(Comparator.comparingInt(Reservation::getPartySize));
            Map<FloorMapItem, Reservation> tableToReservationMapping = new HashMap<>();

            for (Reservation potentialConflict : potentialConflicts) {
                for (FloorMapItem floorMapItem : tables) {
                    if (tableToReservationMapping.containsKey(floorMapItem)) {
                        //table is already reserved
                        continue;
                    }
                    if (potentialConflict.getPartySize() <= floorMapItem.getMaxTableSize() && potentialConflict.getPartySize() >= floorMapItem.getMinTableSize()) {
                        //reserve table
                        tableToReservationMapping.put(floorMapItem, potentialConflict);
                        break;
                    }
                }
            }

            for (FloorMapItem floorMapItem : tables) {
                if (tableToReservationMapping.containsKey(floorMapItem)) {
                    continue;
                } else {
                    if (partySize <= floorMapItem.getMaxTableSize() && partySize >= floorMapItem.getMinTableSize()) {
                        availableTimeSlots.add(timeSlot);
                        break;
                    }
                }
            }
        }
        return availableTimeSlots;
    }*/

    private boolean isTimeSlotAvailableGivenAllExistingReservationTimes(LocalTime timeSlot, List<LocalTime> existingReservationTimes, Duration reservationDuration) {
        for (LocalTime existingReservationTime : existingReservationTimes) {
            if (!isTimeSlotAvailableGivenExistingReservationTime(timeSlot, existingReservationTime, reservationDuration)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTimeSlotAvailableGivenExistingReservationTime(LocalTime potentialTimeSlot, LocalTime existingReservationTime, Duration reservationDuration) {
        if (potentialTimeSlot.isBefore(existingReservationTime)) {
            return potentialTimeSlot.plus(reservationDuration).isBefore(existingReservationTime) || potentialTimeSlot.plus(reservationDuration).equals(existingReservationTime);
        } else {
            return potentialTimeSlot.isAfter(existingReservationTime.plus(reservationDuration)) || potentialTimeSlot.equals(existingReservationTime.plus(reservationDuration));
        }
    }

    private Set<LocalTime> createReservationTimeSlots(LocalTime serviceStartTime, LocalTime serviceEndTime, Duration timeSlotGap) {
        Set<LocalTime> timeSlots = new HashSet<>();
        LocalTime previous = serviceStartTime;
        while (previous.isBefore(serviceEndTime)) {
            timeSlots.add(previous);
            previous = previous.plus(timeSlotGap);
        }
        timeSlots.add(serviceEndTime);
        return timeSlots;
    }

    /*public void add(ReservationDTO reservationDTO) {
        LocalTime requestedReservationTime = reservationDTO.getReservationTime().toLocalTime();
        LocalDate requestedReservationDate = reservationDTO.getReservationTime().toLocalDate();

        Set<LocalTime> allAvailableReservationTimes = findAllAvailableReservationsForDate(reservationDTO.getBusinessId(), requestedReservationDate, reservationDTO.getPartySize());
        if (!allAvailableReservationTimes.contains(requestedReservationTime)) {
            throw new NoAvailableReservationsException(String.format("Requested reservation date time <{}> is not available.", reservationDTO.getReservationTime()));
        }

        Reservation reservation = new Reservation();
        reservation.setBusinessId(reservationDTO.getBusinessId());
        reservation.setGuestId(reservation.getGuestId());
        reservation.setPartyName(reservationDTO.getPartyName());
        reservation.setPartySize(reservationDTO.getPartySize());
        reservation.setReservationTime(reservationDTO.getReservationTime());
        reservation.setNotes(reservationDTO.getNotes());
        reservation.setCompletedIndicator(false);
        repo.save(reservation);
    }*/

    /*public void edit(ReservationDTO reservationDTO) {
        Optional<Reservation> optionalReservation = repo.findById(reservationDTO.getId());
        Reservation existingReservation = optionalReservation.orElseThrow();

        LocalTime requestedReservationTime = reservationDTO.getReservationTime().toLocalTime();
        LocalDate requestedReservationDate = reservationDTO.getReservationTime().toLocalDate();
        int partySize = reservationDTO.getPartySize();

        Set<LocalTime> allAvailableReservationTimes = findAllAvailableReservationsForDate(reservationDTO.getBusinessId(), requestedReservationDate, partySize);
        if (!allAvailableReservationTimes.contains(requestedReservationTime)) {
            throw new NoAvailableReservationsException(String.format("Requested reservation date time <{}> is not available.", reservationDTO.getReservationTime()));
        }

        existingReservation.setPartyName(reservationDTO.getPartyName());
        existingReservation.setReservationTime(reservationDTO.getReservationTime());
        existingReservation.setPartySize(reservationDTO.getPartySize());
        existingReservation.setReservationTime(reservationDTO.getReservationTime());
        existingReservation.setNotes(reservationDTO.getNotes());
        existingReservation.setCompletedIndicator(reservationDTO.isCompletedIndicator());

        repo.save(existingReservation);
    }*/
}
