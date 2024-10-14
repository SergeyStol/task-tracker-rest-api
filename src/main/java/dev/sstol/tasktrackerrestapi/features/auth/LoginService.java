package dev.sstol.tasktrackerrestapi.features.auth;

import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.features.users.UserService;
import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@RequiredArgsConstructor
@Service
public class LoginService {

   private final PasswordEncoder encoder;
   private final UserService userService;
   private final JwtTokenService jwtTokenService;

   public String login(UserLoginDto userLoginDto) {
      User user = (User) userService.loadUserByUsername(userLoginDto.email());
      if (encoder.matches(userLoginDto.password(), user.getPassword())) {
         return jwtTokenService.getToken(user.getUsername(), user.getId(), user.getEmail());
      }
      throw new BadCredentialsException("Bad credentials");
   }


}
