package com.jake.smsgateway.dto;

import lombok.Data;

@Data
public class SmsRequestDTO {
    private String fromNumber;
    private String toNumber;
    private String textNumber;
    private ProcessedIndicator processedInd;
}
