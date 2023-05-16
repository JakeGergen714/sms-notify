package com.jake.auth.repo;

import com.jake.auth.domain.SmsTemplate;
import com.jake.auth.domain.SmsTemplateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsTemplateRepository extends JpaRepository<SmsTemplate, SmsTemplateId> {
    Optional<SmsTemplate> findByTemplateNameAndBusinessId(String templateName, Long businessId);
}
