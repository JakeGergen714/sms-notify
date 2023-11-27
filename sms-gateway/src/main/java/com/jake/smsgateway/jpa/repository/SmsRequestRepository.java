package com.jake.smsgateway.jpa.repository;

import com.jake.smsgateway.jpa.domain.SmsRequest;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SmsRequestRepository extends JpaRepository<SmsRequest, BigInteger> {

    @Query(value = "SELECT * FROM sms_request WHERE processed_ind = :processedInd", nativeQuery = true)
    List<SmsRequest> getSmsRequestsWithUnProcessedIndicator(@Param("processedInd") String processedInd);
}
