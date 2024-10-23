package dev.sstol.tasktrackerrestapi.features.auth;

import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.infrastructure.api.BadRequestException400;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthRestController {

   public static final int JWT_TOKEN_EXPIRY = 60 * 60;

   private final LoginService service;

   public AuthRestController(LoginService loginService) {
      this.service = loginService;
   }

   // POST /auth/register
   @PostMapping(path = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public void register(@ModelAttribute RegisterNewUserDto registerNewUserDto, HttpServletResponse response) {
      log.info("Register: user={}", registerNewUserDto.email());
      validate(registerNewUserDto.email(), registerNewUserDto.password());
      Cookie tokenCookie = getTokenCookie(service.signIn(registerNewUserDto));
      response.addCookie(tokenCookie);
   }

   // POST /auth/login
   @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   @ResponseStatus(HttpStatus.OK)
   ResponseEntity<?> login(@ModelAttribute LoginUserDto loginUserDto, HttpServletResponse response) {
      log.info("Login: user={}", loginUserDto.email());
      validate(loginUserDto.email(), loginUserDto.password());
      Cookie tokenCookie = getTokenCookie(service.login(loginUserDto));
      response.addCookie(tokenCookie);
      return ResponseEntity.ok().build();
   }

   // POST /auth/logout
   @PostMapping("/logout")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void logout(HttpServletResponse response, Authentication authentication) {
      User principal = ((User) authentication.getPrincipal());
      log.info("Logout: user={}", principal.getUsername());
      response.addCookie(getTokenCookie(null,0));
   }

   private void validate(String email, String password) {
      if (Strings.isBlank(email) || Strings.isBlank(password)) {
         throw new BadRequestException400("You should specify password and email");
      }
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
