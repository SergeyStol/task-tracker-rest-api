package dev.sstol.tasktrackerrestapi.features.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthRestController {

   private final LoginService loginService;

   public AuthRestController(LoginService loginService) {
      this.loginService = loginService;
   }

   @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   @ResponseStatus(HttpStatus.OK)
   void loginForm(@RequestParam String email, @RequestParam String password, HttpServletResponse response) {
      login(new UserLoginDto(email, password) ,response);
   }

   @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   ResponseEntity<?> loginJson(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) {
      if (Strings.isBlank(userLoginDto.password()) || Strings.isBlank(userLoginDto.email())) {
         return ResponseEntity.status(400).body(Map.of("message", "You need to specify email and password."));
      }
      login(userLoginDto, response);
      return ResponseEntity.ok().build();
   }

   private void login(UserLoginDto userLoginDto, HttpServletResponse response) {
      String token = loginService.login(userLoginDto);
      Cookie cookie = new Cookie("token", token);
      cookie.setHttpOnly(true);
      cookie.setSecure(false);
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60);
      response.addCookie(cookie);
   }

   @PostMapping("/logout")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void logout(HttpServletResponse response) {
      Cookie cookie = new Cookie("token", null);
      cookie.setHttpOnly(true);
      cookie.setSecure(false);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
   }
}
