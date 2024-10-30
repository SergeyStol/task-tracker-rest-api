package dev.sstol.tasktrackerrestapi.infrastructure.security;

import dev.sstol.tasktrackerrestapi.features.users.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Stol
 * 2024-10-28
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

   private final UserDetailsService userDetailsService;
   private final PasswordEncoder encoder;

   @Override
   public Authentication authenticate(Authentication authentication) throws AuthenticationException {
      try {
         UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
         String password = authentication.getCredentials().toString();
         if (!encoder.matches(password, userDetails.getPassword())) {
            log.warn("Failed login. User: {}. Reason: wrong password.", authentication.getName());
            throw new BadCredentialsException("Bad credentials");
         }
            return UsernamePasswordAuthenticationToken
              .authenticated(userDetails, authentication, userDetails.getAuthorities());
      } catch (Exception e) {
         log.warn("Failed login. User: {}. Reason: wrong login.", authentication.getName());
         throw new BadCredentialsException("Bad credentials");
      }
   }

   public User authenticate(String email, String password) throws AuthenticationException {
      Authentication authentication = authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
      );
      return (User) authentication.getPrincipal();
   }

   @Override
   public boolean supports(Class<?> authentication) {
      return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
   }
}
