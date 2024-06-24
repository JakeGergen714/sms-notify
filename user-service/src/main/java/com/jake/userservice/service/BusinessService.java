package com.jake.userservice.service;

import com.jake.datacorelib.business.dto.BusinessDTO;
import com.jake.datacorelib.business.jpa.Business;
import com.jake.datacorelib.business.jpa.BusinessRepository;
import com.jake.datacorelib.subscription.jpa.SubscriptionRepository;
import com.jake.datacorelib.user.jpa.User;
import com.jake.datacorelib.user.jpa.UserRepository;
import com.jake.userservice.exception.UserAlreadyOwnsABusiness;
import com.jake.userservice.exception.UserNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Log4j2
@RequiredArgsConstructor
public class BusinessService {
    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void t() {
        BusinessDTO dto = new BusinessDTO();
        dto.setName("Peppy's Dinner");
        addNewBusiness(dto, "test");
    }

    public Business addNewBusiness(BusinessDTO businessDTO, String ownerUsername) {
        log.info("searchnig for user <{}>",ownerUsername );
        Optional<User> optionalUser = userRepository.findByUsername(ownerUsername);

        User businessOwner = optionalUser.orElseThrow(()->new UserNotFoundException(ownerUsername));

        log.info("found user");
        if(businessOwner.getBusiness() != null) {
            throw new UserAlreadyOwnsABusiness(ownerUsername, businessOwner.getBusiness().getBusinessId());
        }

        Business newBusiness = modelMapper.map(businessDTO, Business.class);
        businessOwner.setBusiness(newBusiness);

        newBusiness.setUsers(Set.of(businessOwner));

        log.info("adding business", newBusiness);
        return businessRepository.save(newBusiness);
    }
}
