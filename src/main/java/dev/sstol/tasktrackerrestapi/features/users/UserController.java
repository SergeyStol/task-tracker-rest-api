package dev.sstol.tasktrackerrestapi.features.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sergey Stol
 * 2024-10-07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

   private final UserService service;

//   @PostMapping
//   public String register(Authentication authentication) {
//      Instant now = Instant.now();
//      long expiry = 36000L;
//      String scope = authentication.getAuthorities().stream()
//        .map(GrantedAuthority::getAuthority)
//        .collect(Collectors.joining(" "));
//      JwtClaimsSet claims = JwtClaimsSet.builder()
//        .issuer("self")
//        .issuedAt(now)
//        .expiresAt(now.plusSeconds(expiry))
//        .subject(authentication.getName())
//        .claim("scope", scope)
//        .build();
//      return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
//   }

   @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public void registerFromJson(@RequestBody NewUserDto newUserDto, HttpServletResponse response) {
      register(newUserDto, response);
   }

   private void register(NewUserDto newUserDto, HttpServletResponse response) {
      String token = service.saveAndGetToken(newUserDto);
      Cookie cookie = new Cookie("token", token);
      cookie.setHttpOnly(true);
      cookie.setSecure(false); //https
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60 * 24);
      response.addCookie(cookie);
   }

   @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   @ResponseStatus(HttpStatus.OK)
   public void registerFromForm(@ModelAttribute NewUserDto newUserDto, HttpServletResponse response) {
      register(newUserDto, response);
   }

//   @GetMapping
//   @ResponseStatus(HttpStatus.OK)
//   public UserDto getUser(Authentication authentication) {
//      return service.getUserByEmail(authentication.getName());
//   }

   @GetMapping("/me")
   @ResponseStatus(HttpStatus.OK)
   public UserDto getCurrentUser(Authentication authentication) {
      return service.getUserByEmail(authentication.getName());
   }
}
