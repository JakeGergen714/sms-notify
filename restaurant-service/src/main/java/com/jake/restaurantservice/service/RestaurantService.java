package com.jake.restaurantservice.service;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import com.jake.datacorelib.serviceschedule.jpa.ServiceScheduleRepository;
import com.jake.datacorelib.servicetype.jpa.ServiceTypeRepository;
import com.jake.restaurantservice.utility.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceScheduleRepository serviceScheduleRepository;
    private final Mapper mapper;

    public Restaurant addRestaurant(RestaurantDTO dto, long businessId) {
        Restaurant restaurant =  restaurantRepository.save(mapper.toEntity(dto));
        restaurant.setBusinessId(businessId);
        return restaurantRepository.save(restaurant);
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
