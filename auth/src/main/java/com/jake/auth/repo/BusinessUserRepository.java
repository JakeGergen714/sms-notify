package com.jake.auth.repo;

import com.jake.auth.domain.BusinessUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessUserRepository extends JpaRepository<BusinessUser, String> {

}
