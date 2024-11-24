package dev.sstol.tasktrackerrestapi.features.users;

import dev.sstol.tasktrackerrestapi.infrastructure.metrics.CustomMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
   private final CustomMetrics customMetrics;

   @GetMapping("/me")
   @ResponseStatus(HttpStatus.OK)
   public UserDto getCurrentUser(Authentication authentication) {
      customMetrics.incrementCounter();
      return service.getUserByEmail(authentication.getName());
   }
}
