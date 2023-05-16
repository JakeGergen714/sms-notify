package com.jake.auth.domain;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class SmsTemplateId {
    private String templateName;
    private Long businessId;
}
