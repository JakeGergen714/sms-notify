package com.jake.restaurantservice.service;

import com.jake.datacorelib.restaurant.dto.RestaurantDTO;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMap;
import com.jake.datacorelib.restaurant.floormap.jpa.FloorMapRepository;
import com.jake.datacorelib.restaurant.jpa.Restaurant;
import com.jake.datacorelib.restaurant.jpa.RestaurantRepository;
import com.jake.datacorelib.restaurant.servicetype.dto.ServiceTypeDTO;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceType;
import com.jake.datacorelib.restaurant.servicetype.jpa.ServiceTypeRepository;
import com.jake.datacorelib.restaurant.servicetype.serviceschedule.jpa.ServiceScheduleRepository;
import com.jake.datacorelib.subscription.SubscriptionType;
import com.jake.datacorelib.subscription.jpa.Subscription;
import com.jake.datacorelib.user.jpa.RoleType;
import com.jake.datacorelib.user.jpa.User;
import com.jake.datacorelib.user.jpa.UserRepository;
import com.jake.datacorelib.user.jpa.UserRole;
import com.jake.restaurantservice.exception.RestaurantNotFoundException;
import com.jake.restaurantservice.utility.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Log4j2
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceScheduleRepository serviceScheduleRepository;
    private final FloorMapRepository floorMapRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;
    private final GatewayService gatewayService;


    @Transactional
    public RestaurantDTO addRestaurant(RestaurantDTO dto, String username) {
        User restaurantOwner = userRepository.findByUsername(username).orElseThrow();

        Restaurant restaurantEntity = mapper.toEntity(dto);
        log.info("Mapped to Entity <{}>", restaurantEntity);
        restaurantEntity.setBusinessId(restaurantOwner.getBusiness().getBusinessId());
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(SubscriptionType.FREE);
        subscription.setRestaurant(restaurantEntity);
        restaurantEntity.setSubscription(subscription);

        Restaurant savedRestaurant =  restaurantRepository.save(restaurantEntity);
        UserRole restaurantOwnerRole = new UserRole();
        restaurantOwnerRole.setUser(restaurantOwner);
        restaurantOwnerRole.setRoleType(RoleType.OWNER);
        restaurantOwnerRole.setRestaurant(savedRestaurant);
        if(restaurantOwner.getRoles() == null) {
            restaurantOwner.setRoles(Set.of(restaurantOwnerRole));
        }
         else {
            restaurantOwner.getRoles().add(restaurantOwnerRole);
        }
         userRepository.save(restaurantOwner);
        return mapper.toDTO(savedRestaurant);
    }

    public Set<RestaurantDTO> findRestaurantsByBusinessId(long businessId) {
        List<Restaurant> restaurants = restaurantRepository.findAllByBusinessId(businessId, Sort.by("restaurantId"));
        log.info("Found Restaurants {}", restaurants);
        return restaurants.stream().map(mapper::toDTO).collect(Collectors.toSet());
    }

    public RestaurantDTO findRestaurantById(Long restaurantId) {
        Restaurant restaurant= restaurantRepository.findById(restaurantId).orElseThrow(()->new RestaurantNotFoundException(restaurantId));
        log.info("Found restaunt <{}>", restaurant);
        return mapper.toDTO(restaurant);
    }

    public ServiceTypeDTO addServiceType(ServiceTypeDTO serviceTypeDTO, Set<Long> authorizedRestaurantId) {
        ServiceType serviceType = mapper.toEntity(serviceTypeDTO);

        Restaurant restaurant= restaurantRepository
                .findById(serviceTypeDTO.getRestaurantId())
                .orElseThrow(()->new RestaurantNotFoundException(serviceTypeDTO.getRestaurantId()));

        if(!authorizedRestaurantId.contains(restaurant.getRestaurantId())) {
            throw new RestaurantNotFoundException(restaurant.getRestaurantId());
        }

        List<FloorMap> floorMaps = floorMapRepository.findAllById(serviceTypeDTO.getFloorMapIds());
        log.info("Found floor Maps. <{}>", floorMaps);

        serviceType.setFloorMaps(floorMaps);
        serviceType.setServiceTypeId(null);
        serviceType.setRestaurant(restaurant);

        serviceType.getServiceSchedules().stream().forEach(serviceSchedule -> serviceSchedule.setServiceType(serviceType));

        log.info("saving floor type <{}>", serviceType);
        ServiceType saved = serviceTypeRepository.save(serviceType);
        log.info("Saved service type <{}>", serviceType);

        floorMaps.forEach(floorMap->{
            List<ServiceType> serviceTypes = floorMap.getServiceTypes();
            if(serviceTypes==null) {
                floorMap.setServiceTypes(List.of(saved));
            } else {
                serviceTypes.add(saved);
            }
        });

        floorMapRepository.saveAll(floorMaps);

        return mapper.toDto(saved);
    }

    public ServiceTypeDTO updateServiceType(ServiceTypeDTO serviceTypeDTO) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeDTO.getServiceTypeId()).orElseThrow();

        ServiceType dtoToEntity = mapper.toEntity(serviceTypeDTO);

        serviceType.setServiceSchedules(dtoToEntity.getServiceSchedules());
        serviceType.setName(dtoToEntity.getName());
        //serviceType.setFloorMap(dtoToEntity.getFloorMap());

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
