package com.jake.auth.service;

import com.jake.auth.jpa.domain.User;
import com.jake.auth.jpa.domain.UserRole;
import com.jake.auth.jpa.repo.UserRepository;
import com.jake.auth.jpa.repo.UserRoleRepository;
import com.jake.auth.userdetails.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;
    private final UserRoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void t() {
        if(userRepo.findByUsername("test") == null ) {
            User user = new User();
            user.setUsername("test");
            user.setPassword(encoder.encode("test"));
            user.setId(5L);
            userRepo.save(user);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Looking user up by username <{}>", username);
        User user = userRepo.findByUsername(username);
        user.setBusinessId(1L); //todo
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<UserRole> roles = roleRepo.findAllByUserId(user.getId());

        return new UserDetailsImpl(user, roles);
    }
}
