package com.jake.auth.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "sms_template")
@IdClass(SmsTemplateId.class)
public class SmsTemplate {
    @Id
    private String templateName;
    @Id
    private Long businessId;
    private String templateDescription;
    private String template;

}
