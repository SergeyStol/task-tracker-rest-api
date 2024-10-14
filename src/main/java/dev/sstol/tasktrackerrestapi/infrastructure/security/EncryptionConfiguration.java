package dev.sstol.tasktrackerrestapi.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Sergey Stol
 * 2024-10-12
 */
@Configuration
public class EncryptionConfiguration {
   @Bean
   PasswordEncoder passwordEncoder() {
      return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
   }
}
