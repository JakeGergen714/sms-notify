package com.jake.smsgateway.reader;

import com.jake.smsgateway.dto.ProcessedIndicator;
import com.jake.smsgateway.jpa.domain.SmsRequest;
import com.jake.smsgateway.jpa.repository.SmsRequestRepository;
import com.jake.smsgateway.service.SmsRequestService;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
public class SmsRequestReader implements ItemReader<SmsRequest> {
  private final SmsRequestService smsRequestService;

  List<SmsRequest> smsRequests;
  int smsRequestIndex = 0;

  @Override
  public SmsRequest read() throws Exception {
    if (smsRequests == null) {
      smsRequests = smsRequestService.getUnprocessedSmsRequests();
      log.info("Found <{}> Unprocessed SMS Requests.", smsRequests.size());
    }

    if (smsRequests.isEmpty()) {
      log.info("No Unprocessed SMS Requests.");
      smsRequests=null;
      smsRequestIndex=0;
      return null;
    }

    if (smsRequestIndex > smsRequests.size() - 1) {
      log.info("No Unprocessed SMS Requests.");
      smsRequests=null;
      smsRequestIndex=0;
      return null;
    }
    SmsRequest readReq = smsRequests.get(smsRequestIndex++);
    log.info(readReq);
    return readReq;
  }
}
