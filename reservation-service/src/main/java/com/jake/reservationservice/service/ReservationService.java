package com.jake.reservationservice.service;

import com.jake.datacorelib.floormap.jpa.FloorMap;
import com.jake.datacorelib.floormap.jpa.FloorMapItem;
import com.jake.datacorelib.reservation.dto.ReservationDTO;
import com.jake.datacorelib.reservation.jpa.Reservation;
import com.jake.datacorelib.reservation.jpa.ReservationRepository;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.serviceschedule.jpa.ServiceSchedule;
import com.jake.datacorelib.servicetype.jpa.ServiceType;
import com.jake.reservationservice.exception.ReservationNotAvailableException;
import com.jake.reservationservice.exception.TableReservedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Log4j2
public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    public List<Reservation> getAllReservationForDate(Long restaurantId, LocalDate date) {
        List<Reservation> reservations = reservationRepo.findAllByRestaurantIdAndReservationDate(restaurantId, date, Sort.unsorted());

        return reservations;
    }

    public Reservation addReservation(Long restaurantId, ReservationDTO reservationRequest) {
        Restaurant restaurant = restaurantService.findRestaurantByRestaurantId(restaurantId);
        List<Reservation> existingReservations = getAllReservationForDate(restaurantId, reservationRequest.getReservationDate());
        Duration reservationLength = Duration.ofHours(2);
        Optional<ServiceType> optionaRequestedServiceType = restaurant.getServiceTypes().stream().filter(serviceType -> {
            Optional<ServiceSchedule> optionalServiceScheduleForRequestedDay = serviceType.getServiceSchedules().stream().filter(serviceSchedule -> serviceSchedule.
                                                                                                  getDayOfWeek()
                                                                                                  .equals(reservationRequest.getReservationDate().getDayOfWeek()))
                                                                                          .findFirst();
            if (optionalServiceScheduleForRequestedDay.isEmpty()) {
                //This service type is not active on the request day
                return false;
            }
            ServiceSchedule serviceScheduleForRequestedDay = optionalServiceScheduleForRequestedDay.get();
            return serviceScheduleForRequestedDay.getStartTime().isAfter(reservationRequest.getReservationTime())
                    && serviceScheduleForRequestedDay.getEndTime().isBefore(reservationRequest.getReservationTime().plus(reservationLength));
        }).findFirst();

        if (optionaRequestedServiceType.isEmpty()) {
            throw new ReservationNotAvailableException(
                    String.format("Restaurant is not in service on requested day <%s> and time <%s>",
                            reservationRequest.getReservationDate(),
                            reservationRequest.getReservationTime())
            );
        }
        ServiceType activeServiceAtRequestedReservationDateTime = optionaRequestedServiceType.get();
        Set<LocalTime> availableReservationTimes = getAvailableTimeSlotsForServiceOnDay(reservationRequest.getReservationDate(), reservationRequest.getPartySize(), activeServiceAtRequestedReservationDateTime, existingReservations, reservationLength);

        if (!availableReservationTimes.contains(reservationRequest.getReservationTime())) {
            throw new ReservationNotAvailableException(String.format("Restaurant is not available on requested day <%s> and time <%s>",
                    reservationRequest.getReservationDate(),
                    reservationRequest.getReservationTime()));
        }

        Reservation reservation = modelMapper.map(reservationRequest, Reservation.class);
        reservation.setReservationId(null);
        return reservationRepo.save(reservation);

    }


    public Map<LocalDate, Set<LocalTime>> getAllAvailableReservationsBetweenInclusive(Long restaurantId, LocalDate start, LocalDate end, int partySize) {
        Restaurant restaurant = restaurantService.findRestaurantByRestaurantId(restaurantId);
        List<Reservation> existingReservations = reservationRepo.findAllByRestaurantIdAndReservationDateBetween(restaurantId, start, end, Sort.unsorted());

        Set<ServiceType> servicesTypes = restaurant.getServiceTypes();
        Duration reservationLength = Duration.ofHours(1).plusMinutes(30);

        Map<LocalDate, Set<LocalTime>> timeSlotsAvailableForDate = new HashMap<>();
        LocalDate currentDate = start;
        while (!currentDate.isAfter(end)) {
            timeSlotsAvailableForDate.put(currentDate, getAvailableReservationsForDate(currentDate, partySize, restaurant, existingReservations, reservationLength));
            currentDate = currentDate.plusDays(1);
        }

        return timeSlotsAvailableForDate;
    }

    public Set<LocalTime> getAvailableReservationsForDate(LocalDate localDate, int partySize, Restaurant restaurant, List<Reservation> existingReservations, Duration reservationLength) {
        Set<LocalTime> availableTimeSlots = new HashSet<>();

        Set<ServiceType> servicesTypes = restaurant.getServiceTypes();
        Set<Reservation> reservationsOnCurrentDate = existingReservations
                .stream()
                .filter(reservation -> reservation.getReservationDate().equals(localDate))
                .collect(Collectors.toSet());

        Set<ServiceType> activeServiceTypesOnCurrentDate = servicesTypes
                .stream()
                .filter(serviceType -> {
                    Set<ServiceSchedule> schedules = serviceType.getServiceSchedules();
                    return schedules.stream().filter(schedule -> schedule.getDayOfWeek().equals(localDate.getDayOfWeek())).findAny().isPresent();
                })
                .collect(Collectors.toSet());

        for (ServiceType serviceType : activeServiceTypesOnCurrentDate) {
            Set<LocalTime> availableTimeSlotsForService = getAvailableTimeSlotsForServiceOnDay(localDate, partySize, serviceType, existingReservations, reservationLength);
            availableTimeSlots.addAll(availableTimeSlotsForService);
        }

        return availableTimeSlots;
    }

    public Set<LocalTime> getAvailableTimeSlotsForServiceOnDay(LocalDate localDate, int partySize, ServiceType serviceType, List<Reservation> existingReservations, Duration reservationLength) {
        Set<Reservation> reservationsOnCurrentDate = existingReservations
                .stream()
                .filter(reservation -> reservation.getReservationDate().equals(localDate))
                .collect(Collectors.toSet());

        Optional<ServiceSchedule> optionalServiceSchedule = serviceType.getServiceSchedules()
                                                                       .stream()
                                                                       .filter(schedule -> schedule.getDayOfWeek().equals(localDate.getDayOfWeek())).findFirst();

        if (!optionalServiceSchedule.isPresent()) {
            return Set.of();
        }

        ServiceSchedule serviceSchedule = optionalServiceSchedule.get();

        Set<Reservation> reservationsForService = reservationsOnCurrentDate.stream().filter(reservation -> {
            return reservation.getReservationTime().isAfter(serviceSchedule.getStartTime())
                    && reservation.getReservationTime().isBefore(serviceSchedule.getEndTime());
        }).collect(Collectors.toSet());

        FloorMap floorMapForService = serviceType.getFloorMap();

        Map<FloorMapItem, Set<Reservation>> floorMapItemReservations = assignReservationToSmallestPossibleTable(floorMapForService, reservationsForService, reservationLength);

        Map<FloorMapItem, Set<LocalTime>> availableTimeSlotsForFloorMapItem = new HashMap<>();
        Set<FloorMapItem> availableTablesForPartySize = floorMapForService.getFloorMapItems().stream().filter(table -> {
            return table.getMinTableSize() >= partySize && table.getMaxTableSize() <= partySize;
        }).collect(Collectors.toSet());

        for (FloorMapItem floorMapItem : availableTablesForPartySize) {
            if (!floorMapItemReservations.containsKey(floorMapItem)) {
                availableTimeSlotsForFloorMapItem.put(floorMapItem, createReservationTimeSlots(serviceSchedule.getStartTime(), serviceSchedule.getEndTime(), Duration.ofMinutes(30)));
            } else {
                availableTimeSlotsForFloorMapItem.put(floorMapItem, createReservationTimeSlots(serviceSchedule.getStartTime(), serviceSchedule.getEndTime(), Duration.ofMinutes(30))).stream()
                                                 .filter(timeSlot ->
                                                         !isTimeSlotConflicting(timeSlot,
                                                                 reservationsForService.stream()
                                                                                       .map(Reservation::getReservationTime)
                                                                                       .collect(Collectors.toSet()),
                                                                 reservationLength))
                                                 .collect(Collectors.toSet());
            }
        }

        return availableTimeSlotsForFloorMapItem.values().stream()
                                                .flatMap(Set::stream) // Convert each Set<LocalTime> into a stream of LocalTime
                                                .collect(Collectors.toSet()); // Collects all LocalTime objects into a single Set, removing duplicates;
    }

    public Map<FloorMapItem, Set<Reservation>> assignReservationToSmallestPossibleTable(FloorMap floorMap, Set<Reservation> reservations, Duration reservationLength) {
        Set<FloorMapItem> sortedReservableFloorMapItems = floorMap.getFloorMapItems()
                                                                  .stream()
                                                                  .filter(FloorMapItem::isReservable)
                                                                  .collect(Collectors.toCollection(() ->
                                                                          new TreeSet<>(Comparator.comparingInt(FloorMapItem::getMaxTableSize))));

        Map<FloorMapItem, Set<Reservation>> floorMapItemReservations = new HashMap<>();

        for (Reservation reservationForService : reservations) {
            Optional<FloorMapItem> optionalSmallestAvailableTable = sortedReservableFloorMapItems.stream()
                                                                                                 .filter(item -> isTableAvailableForReservation(item, reservationForService, floorMapItemReservations, reservationLength))
                                                                                                 .findFirst();
            if (optionalSmallestAvailableTable.isPresent()) {
                FloorMapItem smallestAvailableTable = optionalSmallestAvailableTable.get();
                floorMapItemReservations.computeIfAbsent(smallestAvailableTable, k -> new HashSet<>()).add(reservationForService);
            } else {
                throw new TableReservedException(String.format("No available tables found for the reservation <%s>!", reservationForService));
            }
        }

        return floorMapItemReservations;
    }


    private boolean isTableAvailableForReservation(FloorMapItem floorMapItem, Reservation reservation, Map<FloorMapItem, Set<Reservation>> reservationsMap, Duration duration) {
        Set<Reservation> reservations = reservationsMap.getOrDefault(floorMapItem, Collections.emptySet());
        return reservations.stream()
                           .noneMatch(existing -> isTimeSlotConflicting(reservation.getReservationTime(), existing.getReservationTime(), duration));
    }

    private Set<LocalTime> createReservationTimeSlots(LocalTime serviceStartTime, LocalTime serviceEndTime, Duration timeSlotGap) {
        Set<LocalTime> timeSlots = new HashSet<>();
        LocalTime nextSlot = serviceStartTime;
        while (nextSlot.isBefore(serviceEndTime)) {
            timeSlots.add(nextSlot);
            nextSlot = nextSlot.plus(timeSlotGap);
        }
        return timeSlots;
    }

    private boolean isTimeSlotConflicting(LocalTime newReservationTime, LocalTime existingReservationTime, Duration duration) {
        LocalTime endOfNewReservation = newReservationTime.plus(duration);
        LocalTime endOfExistingReservation = existingReservationTime.plus(duration);
        return newReservationTime.isBefore(endOfExistingReservation) && endOfNewReservation.isAfter(existingReservationTime);
    }

    private boolean isTimeSlotConflicting(LocalTime newReservationTime, Set<LocalTime> existingReservationTimes, Duration duration) {
        for (LocalTime existingReservationTime : existingReservationTimes) {
            if (isTimeSlotConflicting(newReservationTime, existingReservationTime, duration)) {
                return true;
            }
        }
        return false;
    }
}
