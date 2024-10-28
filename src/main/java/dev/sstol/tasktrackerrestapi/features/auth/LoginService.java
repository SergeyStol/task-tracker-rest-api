package dev.sstol.tasktrackerrestapi.features.auth;

import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.features.users.UserService;
import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

   private final PasswordEncoder encoder;
   private final UserService userService;
   private final JwtTokenService jwtTokenService;

   public String login(LoginUserDto loginUserDto) {
      User user = (User) userService.loadUserByUsername(loginUserDto.email());
      if (encoder.matches(loginUserDto.password(), user.getPassword())) {
         return jwtTokenService.getToken(user.getUsername(), user.getId(), user.getEmail());
      }
      throw new BadCredentialsException("Bad credentials");
   }

   public String signIn(RegisterNewUserDto registerNewUserDto) {
      return userService.saveAndGetToken(registerNewUserDto);
   }
}
