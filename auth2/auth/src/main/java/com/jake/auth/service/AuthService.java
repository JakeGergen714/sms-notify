package com.jake.auth.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jake.auth.dto.AuthTokenDTO;
import com.jake.auth.dto.UserCredentialDTO;
import com.jake.auth.jpa.domain.UserCredential;
import com.jake.auth.jpa.repository.UserCredentialRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {
    private final String REFRESH_TOKEN_CLAIM = "isRefreshToken";
    private final UserCredentialRepository userCredentialRepo;

    @Value("${auth.secret}")
    private String authSecret;

    public Optional<AuthTokenDTO> signIn(UserCredentialDTO givenUserCredential) throws UnsupportedEncodingException {
        Optional<UserCredential> optionalUserCredential = userCredentialRepo.findById(Objects.requireNonNull(givenUserCredential.getUsername()));

        if (optionalUserCredential.isEmpty()) {
            return Optional.empty();
        }

        UserCredential existingUserCredential = optionalUserCredential.get();

        if (!validatePassword(givenUserCredential.getPassword(), existingUserCredential.getPassword())) {
            log.info("invalid password");
            return Optional.empty();
        }

        log.info("User Signed in. Generating Auth Token");
        return Optional.of(new AuthTokenDTO(generateAccessToken(givenUserCredential.getUsername()), generateRefreshToken(givenUserCredential.getUsername())));
    }

    public Optional<AuthTokenDTO> signUp(UserCredentialDTO givenUserCredential) throws UnsupportedEncodingException {
        Optional<UserCredential> optionalUserCredential = userCredentialRepo.findById(Objects.requireNonNull(givenUserCredential.getUsername()));

        if (optionalUserCredential.isPresent()) {
            log.info("Cant signup <{}> user already exists", givenUserCredential.getUsername());
            return Optional.empty();
        }

        String hashedPassword = hashPassword(givenUserCredential.getPassword());
        userCredentialRepo.save(new UserCredential(givenUserCredential.getUsername(), hashedPassword));

        log.info("User Sign Up. Generating Auth Token");
        return Optional.of(new AuthTokenDTO(generateAccessToken(givenUserCredential.getUsername()), generateRefreshToken(givenUserCredential.getUsername())));
    }

    public boolean validate(AuthTokenDTO authToken) {
        log.info("validate <{}>", authToken);
        return validateAccessToken(authToken.getAccessToken());
    }

    public boolean validateJwt(String jwt) {
        log.info("validate <{}>", jwt);
        return validateAccessToken(jwt);
    }

    public String getUserName(String jwt) {
        Optional<DecodedJWT> decodedJWT = this.decodedJWT(jwt, authSecret);
        if(decodedJWT.isEmpty()){
            return null;
        }
        return decodedJWT.get().getSubject();
    }

    public Optional<AuthTokenDTO> refresh(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            return Optional.empty();
        }

        Optional<DecodedJWT> optionalDecodedRefreshToken = decodedJWT(refreshToken, this.authSecret);

        if (optionalDecodedRefreshToken.isEmpty()) {
            log.info("Refresh failed. Could not refresh access token");
            return Optional.empty();
        }
        DecodedJWT decodedRefreshToken = optionalDecodedRefreshToken.get();
        Boolean isRefreshToken = decodedRefreshToken.getClaim(REFRESH_TOKEN_CLAIM).asBoolean();

        if (isRefreshToken == null || !isRefreshToken) {
            log.info("Refresh token given was not a real refresh token");
            return Optional.empty();
        }

        String username = decodedRefreshToken.getSubject();
        try {
            log.info("Generating new access and refresh token for user <{}>", username);
            return Optional.of(new AuthTokenDTO(generateAccessToken(username), generateRefreshToken(username)));
        } catch (Exception e) {
            log.error("Failed to create new auth token", e);
            return Optional.empty();
        }
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(12, plainPassword.toCharArray());
    }

    private boolean validatePassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }

    private boolean validateAccessToken(String accessToken) {
        return validateJWT(accessToken, authSecret);
    }

    private boolean validateRefreshToken(String refreshToken) {
        return validateJWT(refreshToken, authSecret);
    }

    private boolean validateJWT(String jwt, String secret) {
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    // reusable verifier instance
                    .build();
            decodedJWT = verifier.verify(jwt);
        } catch (JWTVerificationException exception) {
            log.error("Invalid JWT", exception);
            return false;
        } catch (UnsupportedEncodingException e) {
            log.error("Error while validating access token.", e);
            return false;
        }

        return true;
    }

    private Optional<DecodedJWT> decodedJWT(String jwt, String authSecret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(authSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            return Optional.of(verifier.verify(jwt));
        } catch (JWTVerificationException exception) {
            log.error("Invalid JWT", exception);
            return Optional.empty();
        } catch (UnsupportedEncodingException e) {
            log.error("Error while validating access token.", e);
            return Optional.empty();
        }
    }


    private String generateRefreshToken(String username) throws UnsupportedEncodingException {
        return JWT.create().withExpiresAt(Date.from(Instant.now().plusSeconds(20))).withSubject(username).withClaim(REFRESH_TOKEN_CLAIM, true).sign(Algorithm.HMAC256(authSecret));
    }

    private String generateAccessToken(String username) throws UnsupportedEncodingException {
        return JWT.create().withExpiresAt(Date.from(Instant.now().plusSeconds(10))).withSubject(username).sign(Algorithm.HMAC256(authSecret));
    }

}
