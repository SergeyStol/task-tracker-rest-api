package dev.sstol.tasktrackerrestapi.infrastructure.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

   private final JwtEncoder jwtEncoder;
   private final JwtDecoder jwtDecoder;

   public String getToken(String subject, Long userId, String userEmail) {
      Instant now = Instant.now();
      JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("task-tracker-app")
        .issuedAt(now)
        .expiresAt(now.plusSeconds(36000L))
        .subject(subject)
        .claim("userId", userId)
        .claim("userEmail", userEmail)
        .build();
      String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
      log.info("Generated new jwt token for user={}", userEmail);
      return tokenValue;
   }

   public JwtDecodedToken getDecodedToken(String encodedToken) {
      return new JwtDecodedToken(jwtDecoder.decode(encodedToken));
   }

}
