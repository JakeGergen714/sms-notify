package com.jake.auth.jpa.repository;

import com.jake.auth.jpa.domain.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
}
