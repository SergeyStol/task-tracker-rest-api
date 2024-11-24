package dev.sstol.tasktrackerrestapi.features.auth;

import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.BadRequestException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

   public static final int JWT_TOKEN_EXPIRY = 60 * 60;

   private final AuthService service;

   public AuthController(AuthService authService) {
      this.service = authService;
   }

   // POST /auth/register
   @PostMapping(path = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public void registerUser(@ModelAttribute NewUserDto newUserDto, HttpServletResponse response) {
      log.info("Register: user={}", newUserDto.email());
      if (invalid(newUserDto.email(), newUserDto.password())) {
         throw new BadRequestException("You should specify password and email");
      }
      Cookie tokenCookie = getTokenCookie(service.register(newUserDto));
      response.addCookie(tokenCookie);
   }

   // POST /auth/login
   @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   @ResponseStatus(HttpStatus.OK)
   ResponseEntity<?> login(@ModelAttribute LoginUserDto loginUserDto, HttpServletResponse response) {
      log.info("Login: user={}", loginUserDto.email());
      if (invalid(loginUserDto.email(), loginUserDto.password())) {
         throw new BadCredentialsException("Bad credentials");
      }
      Cookie tokenCookie = getTokenCookie(service.login(loginUserDto));
      response.addCookie(tokenCookie);
      return ResponseEntity.ok().build();
   }

   private boolean invalid(String email, String password) {
      return Strings.isBlank(email) || Strings.isBlank(password);
   }

   private Cookie getTokenCookie(String tokenValue) {
      return getTokenCookie(tokenValue, JWT_TOKEN_EXPIRY);
   }

   private Cookie getTokenCookie(String tokenValue, int expiry) {
      Cookie cookie = new Cookie("token", tokenValue);
      cookie.setHttpOnly(true);
      cookie.setSecure(false);
      cookie.setPath("/");
      cookie.setMaxAge(expiry);
      return cookie;
   }
}
