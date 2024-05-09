package com.jake.auth.service;

import com.jake.auth.userdetails.UserDetailsImpl;
import com.jake.datacorelib.user.jpa.User;
import com.jake.datacorelib.user.jpa.UserRepository;
import com.jake.datacorelib.user.jpa.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            user.setUserId(5l);
            userRepo.save(user);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Looking user up by username <{}>", username);
        User user = userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));


        return new UserDetailsImpl(user, user.getRoles());
    }
}
