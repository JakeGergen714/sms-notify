package com.jake.auth.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.jake.auth.dto.UserCredentialDTO;
import com.jake.auth.jpa.domain.UserCredential;
import com.jake.auth.jpa.repository.UserCredentialRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final UserCredentialRepository userCredentialRepo;

    public boolean isUserNameAvailable(String username) {
        return userCredentialRepo.findById(Objects.requireNonNull(username)).isEmpty();
    }

    public boolean signUp(UserCredentialDTO givenUserCredential) throws UnsupportedEncodingException {
        Optional<UserCredential> optionalUserCredential = userCredentialRepo.findById(Objects.requireNonNull(givenUserCredential.getUsername()));

        if (optionalUserCredential.isPresent()) {
            log.error("Cant signup <{}> user already exists.", givenUserCredential.getUsername());
            return false;
        }

        String hashedPassword = hashPassword(givenUserCredential.getPassword());
        userCredentialRepo.save(new UserCredential(givenUserCredential.getUsername(), hashedPassword));
        return true;
    }


    private String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }
}
