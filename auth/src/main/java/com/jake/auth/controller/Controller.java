package com.jake.auth.controller;

import com.jake.auth.domain.AuthSession;
import com.jake.auth.domain.Business;
import com.jake.auth.domain.Credential;
import com.jake.auth.domain.SmsTemplate;
import com.jake.auth.service.BusinessService;
import com.jake.auth.service.BusinessUserService;
import com.jake.auth.service.CredentialService;
import java.util.Optional;

import com.jake.auth.service.SmsTemplateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class Controller {
  @Autowired private CredentialService credService;

  @Autowired private BusinessService businessService;
  @Autowired private BusinessUserService businessUserService;
  @Autowired private SmsTemplateService smsTemplateService;

  @PostMapping(path = "/signup")
  public ResponseEntity<HttpStatus> signup(Credential cred) {
    log.info("/signup");
    if (credService.signup(cred)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping(path = "/authenticate")
  public ResponseEntity<AuthSession> authenticate(Credential cred) {
    Optional<AuthSession> o = credService.authenticate(cred);
    if (o.isPresent()) {
      return ResponseEntity.of(Optional.of(o.get()));
    } else {
      return ResponseEntity.of(Optional.empty());
    }
  }

  @GetMapping(path = "/validate")
  public boolean validate(AuthSession authSession) {
    return credService.validate(authSession);
  }

  @PostMapping(path = "/signout")
  public ResponseEntity<String> signout(AuthSession authSession) {
    log.info("/signout");
    if (authSession.getToken() == null || authSession.getUsername() == null) {
      log.info("Signout failed. Missing username or auth token.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Signout failed. Missing username or auth token");
    }
    if (credService.signout(authSession)) {
      return ResponseEntity.status(HttpStatus.OK).body("Signed out.");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Signout failed.");
  }

  @PostMapping(path = "/addBusiness")
  public ResponseEntity<String> addBuisness(AuthSession authSession, Business business) {
    log.info("/addBusiness");
    if (credService.validate(authSession)) {
      log.info("Validated.");

      log.info("Adding business.");
      Optional<Business> addedBusiness = businessService.signup(business);
      if (addedBusiness.isPresent()) {
        log.info("Added business.");
        log.info("Adding authenticated user to business");

        if (businessUserService.addSelfToBusiness(authSession, addedBusiness.get())) {
          log.info("Added self to business");
          return ResponseEntity.ok("Business added.");
        }
        log.info("Failed to add self to business");
        return ResponseEntity.badRequest().build();
      } else {
        log.info("Failed to add business.");
        return ResponseEntity.badRequest().build();
      }
    }
    log.info("Validation failed.");
    return ResponseEntity.badRequest().build();
  }

  @PostMapping(path = "/addUserToBusiness")
  public ResponseEntity<String> addUserToBusiness(
      AuthSession authSession, Business business, String userToAdd, boolean isAdmin) {
    log.info("/addUserToBusiness");
    if (!credService.validate(authSession)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
      if (businessUserService.addUserToBusiness(authSession, business, userToAdd, isAdmin)) {
      log.info("Added user to business.");
      return ResponseEntity.ok("Added user to business");
    }
    log.info("Failed to add user to business");
    return ResponseEntity.badRequest().body("Failed to add user to business");
  }

  @PostMapping(path = "/addTemplate")
  public ResponseEntity<String> addTemplate(AuthSession authSession, SmsTemplate smsTemplate) {
    if(!credService.validate(authSession)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if(smsTemplateService.addSmsTemplate(authSession, smsTemplate))  {
      return ResponseEntity.ok("Added Sms Template.");
    }
    return ResponseEntity.badRequest().body("Failed to add sms template");
  }
}
