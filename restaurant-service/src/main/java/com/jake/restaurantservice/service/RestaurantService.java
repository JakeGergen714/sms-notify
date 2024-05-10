package com.jake.restaurantservice.service;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceTypeRepository;
import com.jake.datacorelib.restaurant.servicetype.serviceschedule.jpa.ServiceScheduleRepository;
import com.jake.datacorelib.subscription.SubscriptionType;
import com.jake.datacorelib.subscription.jpa.Subscription;
import com.jake.restaurantservice.utility.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Log4j2
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceScheduleRepository serviceScheduleRepository;
    private final FloorMapService floorMapService;
    private final Mapper mapper;

    public RestaurantDTO addRestaurant(RestaurantDTO dto, long businessId) {
        Restaurant restaurantEntity = mapper.toEntity(dto);
        log.info("Mapped to Entity <{}>", restaurantEntity);
        restaurantEntity.setBusinessId(businessId);
        Restaurant restaurant =  restaurantRepository.save(restaurantEntity);
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(SubscriptionType.FREE);
        subscription.setRestaurant(restaurant);
        restaurant.setSubscription(subscription);
        return mapper.toDTO(restaurant);
    }

    public RestaurantDTO findRestaurantByBusinessId(long businessId) {
        Restaurant restaurant = restaurantRepository.findAllByBusinessId(businessId).stream().findAny().orElseThrow();
        return mapper.toDTO(restaurant);
    }

    public ServiceTypeDTO updateServiceType(ServiceTypeDTO serviceTypeDTO) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeDTO.getServiceTypeId()).orElseThrow();

        ServiceType dtoToEntity = mapper.toEntity(serviceTypeDTO);

        serviceType.setServiceSchedules(dtoToEntity.getServiceSchedules());
        serviceType.setName(dtoToEntity.getName());
        serviceType.setFloorMap(dtoToEntity.getFloorMap());

        return mapper.toDto(serviceTypeRepository.save(serviceType));
    }

   /* public Optional<RestaurantDTO> findRestaurantByBusinessId(long businessId) {
      Restaurant restaurant = restaurantRepository.findById(businessId).orElseThrow();
      ServiceType serviceTypes serviceTypeRepository.findAllByBusinessId(businessId);


        return null;
    }

    public ServiceType addServiceType(ServiceTypeDTO dto, long businessId) {
        ServiceType serviceType = new ServiceType();
        serviceType.setRestaurantId(businessId);
        serviceType.setName(dto.getName());
        return serviceTypeRepository.save(serviceType);
    }

    public ServiceSchedule addServiceSchedule(ServiceScheduleDTO dto, long businessId) {
        ServiceSchedule serviceSchedule = new ServiceSchedule();

        serviceSchedule.setServiceId(dto.getServiceId());
        serviceSchedule.setDayOfWeek(dto.getDayOfWeek());
        serviceSchedule.setStartTime(dto.getStartTime());
        serviceSchedule.setEndTime(dto.getEndTime());

        return serviceScheduleRepository.save(serviceSchedule);
    }*/
}
