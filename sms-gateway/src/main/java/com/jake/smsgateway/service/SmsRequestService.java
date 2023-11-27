package com.jake.smsgateway.service;

import com.jake.smsgateway.dto.ProcessedIndicator;
import com.jake.smsgateway.jpa.domain.SmsEndpoint;
import com.jake.smsgateway.jpa.domain.SmsRequest;
import com.jake.smsgateway.jpa.repository.SmsEndpointRepository;
import com.jake.smsgateway.jpa.repository.SmsRequestRepository;

import java.math.BigInteger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
@RequiredArgsConstructor
public class SmsRequestService {
  private final SmsRequestRepository requestRepository;
  private final SmsEndpointRepository endpointRepository;

  private RestTemplate restTemplate = new RestTemplate();

  public List<SmsRequest> getUnprocessedSmsRequests() {
    log.info(ProcessedIndicator.UNPROCESSED.name());
    return requestRepository.getSmsRequestsWithUnProcessedIndicator(
        ProcessedIndicator.UNPROCESSED.name());
  }

  public void smsRequestsProcessed(SmsRequest smsRequest) {
    smsRequest.setProcessedInd(ProcessedIndicator.PROCESSED.name());
    requestRepository.save(smsRequest);
  }

  public void sendSmsRequestToSmsEndpoint(SmsRequest smsRequest, SmsEndpoint  smsEndpoint) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("number", smsRequest.getToNumber());
      headers.add("text", smsRequest.getTextMessage());
      HttpEntity<String> requestEntity = new HttpEntity<String>(null, headers);
      log.info(
          restTemplate
              .exchange(smsEndpoint.getUrl(), HttpMethod.POST, requestEntity, Void.class)
              .getStatusCode());
  }
}
