package com.jake.auth.service;

import com.jake.auth.domain.AuthSession;
import com.jake.auth.domain.Business;
import com.jake.auth.domain.BusinessUser;
import com.jake.auth.repo.BusinessUserRepository;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class BusinessUserService {
  @Autowired BusinessUserRepository businessUserRepository;
  @Autowired CredentialService credentialService;

  public boolean addSelfToBusiness(AuthSession authSession, Business business) {
    if (businessUserRepository.findById(authSession.getUsername()).isPresent()) {
      log.info("User name already belongs to a business");
      return false;
    }
    log.info("Adding business.");
    BusinessUser businessUser = new BusinessUser();
    businessUser.setBusinessId(business.getId());
    businessUser.setUsername(authSession.getUsername());
    businessUser.setIsAdmin(true);
    log.info("Adding business user <{}>.", businessUser);
    businessUserRepository.save(businessUser);

    return true;
  }

  public boolean addUserToBusiness(
      AuthSession authSession, Business business, String usernameToAdd, boolean makeNewUserAdmin) {
    log.info("addUserToBusiness");

    Optional<BusinessUser> o = businessUserRepository.findById(authSession.getUsername());

    if (o.isEmpty()) {
      log.info(
          "Failed to add user to business because signed in user does not belong to a business.");
      return false;
    }
    log.info("Checking if signed in user an admin for this business");
    BusinessUser signedInBusinessUser = o.get();
    log.info("Signed in business user <{}>", signedInBusinessUser);
    if (!signedInBusinessUser.getIsAdmin().booleanValue()) {
      log.info(
          "Failed to add user to business because signed in user is not an admin for the business.");
      return false;
    }

    log.info("Checking if user exists");
    if (!credentialService.userExists(usernameToAdd)) {
      log.info("Failed to add user to business because user <{}> does not exist.", usernameToAdd);
      return false;
    }
    log.info("User exists");
    log.info("Adding user <{}> to business <{}>}", usernameToAdd, business.getName());

    BusinessUser newUser = new BusinessUser();
    newUser.setBusinessId(signedInBusinessUser.getBusinessId());
    newUser.setUsername(usernameToAdd);
    newUser.setIsAdmin(makeNewUserAdmin);
    businessUserRepository.save(newUser);
    return true;
  }

  public Optional<BusinessUser> getBusinessUser(AuthSession authSession) {
    return businessUserRepository.findById(authSession.getUsername());
  }
}
