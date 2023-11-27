package com.jake.smsgateway.jpa.repository;

import com.jake.smsgateway.jpa.domain.SmsEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface SmsEndpointRepository extends JpaRepository<SmsEndpoint, BigInteger> {

    List<SmsEndpoint> findAllByOrderByTextsSentAsc();
}
