package com.jake.auth.service;

import com.jake.auth.domain.AuthSession;
import com.jake.auth.domain.Business;
import com.jake.auth.domain.BusinessUser;
import com.jake.auth.repo.BusinessRepository;
import com.jake.auth.repo.BusinessUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class BusinessService {
  @Autowired private BusinessRepository businessRepository;

  public Optional<Business> signup(Business business) {
      if(business.getName() == null) {
          log.info("Business signup failed. name was null.");
          return Optional.empty();
      }

      log.info("Saving Business");
      return Optional.of(businessRepository.save(business));
  }

}
