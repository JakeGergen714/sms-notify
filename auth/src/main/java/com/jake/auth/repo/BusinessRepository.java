package com.jake.auth.repo;

import com.jake.auth.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, BigInteger> {

}
