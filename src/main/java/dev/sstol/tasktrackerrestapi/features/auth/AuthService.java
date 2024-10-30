package dev.sstol.tasktrackerrestapi.features.auth;

import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.features.users.UserService;
import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtTokenService;
import dev.sstol.tasktrackerrestapi.infrastructure.security.UserAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

   private final UserService userService;
   private final JwtTokenService jwtTokenService;
   private final UserAuthenticationProvider authenticationProvider;

   public String login(LoginUserDto loginUserDto) {
      User user = authenticationProvider.authenticate(loginUserDto.email(), loginUserDto.password());
      return jwtTokenService.getToken(user.getUsername(), user.getId(), user.getEmail());
   }

   public String register(NewUserDto newUserDto) {
      User user = userService.save(newUserDto);
      return jwtTokenService.getToken(user.getUsername(), user.getId(), user.getEmail());
   }
}
