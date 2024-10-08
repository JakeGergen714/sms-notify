package com.jake.auth.service;

import com.jake.auth.domain.AuthSession;
import com.jake.auth.domain.BusinessUser;
import com.jake.auth.domain.SmsTemplate;
import com.jake.auth.repo.SmsTemplateRepository;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SmsTemplateService {
  @Autowired private SmsTemplateRepository smsTemplateRepo;

  @Autowired private BusinessUserService businessUserService;

  // user needs to have access to this business
  public boolean addSmsTemplate(AuthSession authSession, SmsTemplate smsTemplate) {
    Optional<BusinessUser> businessUserOptional = businessUserService.getBusinessUser(authSession);

    if (businessUserOptional.isEmpty()) {
      log.error(
          "Failed to add Sms Template for user <{}>. User does not belong to a business",
          authSession.getUsername());
      return false;
    }
    BusinessUser businessUser = businessUserOptional.get();

    SmsTemplate templateToAdd = new SmsTemplate();
    templateToAdd.setTemplateName(smsTemplate.getTemplateName());
    templateToAdd.setBusinessId(businessUser.getBusinessId());
    templateToAdd.setTemplateDescription(smsTemplate.getTemplateDescription());
    templateToAdd.setTemplate(smsTemplate.getTemplate());

    if (smsTemplateRepo
        .findByTemplateNameAndBusinessId(
            smsTemplate.getTemplateName(), businessUser.getBusinessId())
        .isPresent()) {
      log.error("This business already has a template with this name.");
      return false;
    }

    try {
      smsTemplateRepo.save(templateToAdd);
    } catch (Exception e) {
      log.error("Failed to add sms template.");
      return false;
    }
    return true;
  }

  public Optional<SmsTemplate> getSmsTemplate(AuthSession authSession, SmsTemplate smsTemplate) {
    Optional<BusinessUser> businessUserOptional = businessUserService.getBusinessUser(authSession);

    if (businessUserOptional.isEmpty()) {
      log.error(
          "Failed to get Sms Template for user <{}>. User does not belong to a business",
          authSession.getUsername());
      return Optional.empty();
    }
    BusinessUser businessUser = businessUserOptional.get();

    Optional<SmsTemplate> smsTemplateOptional =
        smsTemplateRepo.findByTemplateNameAndBusinessId(
            smsTemplate.getTemplateName(), businessUser.getBusinessId());

    if (smsTemplateOptional.isEmpty()) {
      log.error(
          "Failed to get Sms Template for user <{}>. Could not find Sms template with name <{}>",
          authSession.getUsername(),
          smsTemplate.getTemplateName());
      return Optional.empty();
    }

    SmsTemplate dbSmsTemplate = smsTemplateOptional.get();

    SmsTemplate templateToReturn = new SmsTemplate();
    templateToReturn.setTemplate(dbSmsTemplate.getTemplate());
    templateToReturn.setTemplateName(dbSmsTemplate.getTemplateName());
    templateToReturn.setTemplateDescription(smsTemplate.getTemplateDescription());
    return Optional.of(templateToReturn);
  }
}
