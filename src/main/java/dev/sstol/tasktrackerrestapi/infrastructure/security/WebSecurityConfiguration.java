package dev.sstol.tasktrackerrestapi.infrastructure.security;

import dev.sstol.tasktrackerrestapi.features.users.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

   private final JwtAuthenticationFilter jwtAuthenticationFilter;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
          .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "swagger-ui.html", "/v3/api-docs/**").permitAll()
          .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
          .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()
          .anyRequest().authenticated())
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(
          (request, response, authException) -> {
             response.sendError(
               HttpServletResponse.SC_UNAUTHORIZED,
               "Unauthorized"
             );
          }
        ).accessDeniedHandler((request, response, accessDeniedException) -> {
             response.sendError(
               HttpServletResponse.SC_FORBIDDEN,
               "Forbidden"
             );
          }
        ))
        .logout(conf -> conf
          .logoutUrl("/auth/logout")
          .logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
             if (authentication != null) {
               User principal = ((User) authentication.getPrincipal());
               log.info("Logout: user={}", principal.getUsername());
                response.setStatus(204);
                response.getWriter().flush();
             } else {
                response.setStatus(401);
                response.getWriter().flush();
             }
          })
          .invalidateHttpSession(true)
          .deleteCookies("token")
        )
        .addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .csrf((csrf) -> csrf.ignoringRequestMatchers("/users"))
        .build();
   }

}