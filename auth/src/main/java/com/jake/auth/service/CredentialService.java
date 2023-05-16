package com.jake.auth.service;

import com.jake.auth.domain.AuthSession;
import com.jake.auth.domain.Credential;
import com.jake.auth.repo.AuthSessionRepository;
import com.jake.auth.repo.CredentialRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class CredentialService {
  @Autowired private CredentialRepository credentialRepository;
  @Autowired private AuthSessionRepository authSessionRepository;

  @Value("${auth.secret}")
  private String secret;

  public boolean signup(Credential credential) {
    try {
      if (credentialRepository.existsById(credential.getUsername())) {
        log.info("Username already exists <{}>", credential.getUsername());
        return false;
      }
      log.info("Saving <{}>", credential.getUsername());
      credentialRepository.save(credential);
      return true;
    } catch (Exception e) {
      log.error("Failed to save credential username <{}>", credential.getUsername(), e);
      return false;
    }
  }

  public Optional<AuthSession> authenticate(Credential credential) {
    log.info("Authenticating <{}>", credential.getUsername());
    try {
      Optional<Credential> o = credentialRepository.findById(credential.getUsername());
      if (o.isPresent()) {
        if (o.get().getPassword().equals(credential.getPassword())) {
          log.info("Authenticated <{}>", credential.getUsername());
          String token = createJwt(credential);
          AuthSession authSession = new AuthSession();
          authSession.setUsername(credential.getUsername());
          authSession.setToken(token);

          try {
            log.info("Creating auth session for <{}>", credential.getUsername());
            authSessionRepository.save(authSession);
          } catch (Exception e) {
            log.error("Failed to save auth session for username <{}>", credential.getUsername());
            return Optional.empty();
          }
          log.info("Auth session created for <{}>", credential.getUsername());
          return Optional.of(authSession);
        }
      }
    } catch (Exception e) {
      log.error("Failed to authenticate username <{}>", credential.getUsername(), e);
      return Optional.empty();
    }
    return Optional.empty();
  }

  public boolean validate(AuthSession authSession) {
    log.info("Validating <{}>", authSession.getUsername());
    Optional<AuthSession> dbAuthSession = authSessionRepository.findById(authSession.getUsername());

    if (dbAuthSession.isPresent()) {
      if (authSession.getToken().equals(dbAuthSession.get().getToken())) {
        return validateJwt(authSession);
      }
    }
    log.info("Failed validation");
    return false;
  }

  public boolean signout(AuthSession authSession) {
    log.info("Signing out <{}>", authSession.getUsername());
    if(validate(authSession)) {
      authSessionRepository.delete(authSession);
     log.info("Signed out <{}> successful", authSession.getUsername());
     return true;
    }
    log.info("Failed to validate for signout.");
    return false;
  }

  public boolean userExists(String username) {
    return credentialRepository.findById(username).isPresent();
  }

  private boolean validateJwt(AuthSession authSession) {
    String jwt = authSession.getToken();
    try {
      Claims claim =
          Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();
      log.info("Passed Validation");
      return true;
    } catch (Exception e) {
      log.info("invalid jwt", e);
      return false;
    }
  }

  private String createJwt(Credential cred) {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    byte[] apiKeySecretBytes = Base64.decodeBase64(secret);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    return Jwts.builder()
        .setSubject(String.format("%S", cred.getUsername()))
        .setIssuer("AuthService")
        .setIssuedAt(Date.from(Instant.now()))
        .signWith(getSigningKey(), signatureAlgorithm)
        .compact();
  }

  private Key getSigningKey() {
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] apiKeySecretBytes = Base64.decodeBase64(secret);
    return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
  }
}
