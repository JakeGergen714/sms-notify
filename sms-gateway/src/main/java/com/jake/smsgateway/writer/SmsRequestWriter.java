package com.jake.smsgateway.writer;

import com.jake.smsgateway.jpa.domain.SmsEndpoint;
import com.jake.smsgateway.jpa.domain.SmsRequest;
import com.jake.smsgateway.jpa.repository.SmsEndpointRepository;
import com.jake.smsgateway.service.SmsRequestService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class SmsRequestWriter implements ItemWriter<SmsRequest> {
  private final SmsRequestService service;
  private final SmsEndpointRepository endpointRepo;

  @Override
  public void write(Chunk<? extends SmsRequest> chunk) throws Exception {
    List<SmsEndpoint> endpoints = endpointRepo.findAllByOrderByTextsSentAsc();
    int index = 0;
    for (SmsRequest request : chunk) {
      SmsEndpoint endpoint = endpoints.get(index++);
      if(index > endpoints.size() -1 ){
        index = 0;
      }
      service.sendSmsRequestToSmsEndpoint(request, endpoint);
      endpoint.setTextsSent(endpoint.getTextsSent() + 1);
      service.smsRequestsProcessed(request);
    }
    endpointRepo.saveAll(endpoints);
  }
}
