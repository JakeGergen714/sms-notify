package com.jake.auth.repo;

import com.jake.auth.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, String> {
}
