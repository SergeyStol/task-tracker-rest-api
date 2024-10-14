package dev.sstol.tasktrackerrestapi.infrastructure.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
public record JwtDecodedToken(Jwt token) {
   public String getUserEmail() {
      return token.getClaims().get("userEmail") instanceof String userEmail ? userEmail : null;
   }

   public Long getUserId() {
      return token.getClaims().get("userId") instanceof Long userId ? userId : null;
   }
}
