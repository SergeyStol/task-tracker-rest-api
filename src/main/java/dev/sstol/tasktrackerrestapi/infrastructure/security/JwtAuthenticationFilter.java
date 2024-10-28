package dev.sstol.tasktrackerrestapi.infrastructure.security;

import dev.sstol.tasktrackerrestapi.features.users.UserService;
import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtDecodedToken;
import dev.sstol.tasktrackerrestapi.infrastructure.jwt.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
   private final JwtTokenService jwtTokenService;
   private final UserService userService;

   @Override
   public void doFilterInternal(HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain chain) throws IOException, ServletException {
      Cookie[] cookies = request.getCookies();
      String token = null;
      if (cookies != null) {
         for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
               token = cookie.getValue();
            }
         }
      }

      if (token != null) {
         JwtDecodedToken jwtDecodedToken = jwtTokenService.getDecodedToken(token);

         UserDetails principal;
         try {
            principal = userService.loadUserByUsername(jwtDecodedToken.getUserEmail());
         } catch (Exception e) {
            chain.doFilter(request, response);
            return;
         }
         UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
           principal, null, Collections.emptyList());
         SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      chain.doFilter(request, response);
   }
}