package dev.sstol.tasktrackerrestapi.infrastructure.security;

import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

   private final JwtAuthenticationFilter jwtAuthenticationFilter;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
          .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "swagger-ui.html", "/v3/api-docs/**").permitAll()
          .requestMatchers(HttpMethod.POST, "/users", "/auth/login").permitAll()
          .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//        .csrf((csrf) -> csrf.ignoringRequestMatchers("/users"))
        .build();
   }

}