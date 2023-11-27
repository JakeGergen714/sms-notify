package com.jake.smsgateway.processor;

import com.jake.smsgateway.jpa.domain.SmsRequest;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SmsRequestProcessor implements ItemProcessor<SmsRequest, SmsRequest> {

    @Override
    public SmsRequest process(SmsRequest item) throws Exception {
       return item;
    }
}
