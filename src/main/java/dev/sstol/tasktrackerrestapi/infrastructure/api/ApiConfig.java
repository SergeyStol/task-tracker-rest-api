package dev.sstol.tasktrackerrestapi.infrastructure.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * @author Sergey Stol
 * 2024-10-21
 */
@Configuration
public class ApiConfig {
   @Bean
   public HttpHeaders httpHeaders() {
      var httpHeaders = new HttpHeaders();
      httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
      return httpHeaders;
   }
}
