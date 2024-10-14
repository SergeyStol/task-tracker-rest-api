package dev.sstol.tasktrackerrestapi.infrastructure.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Stol
 * 2024-10-13
 */
@Configuration
public class MappersConfig {
   @Bean
   ObjectMapper objectMapper() {
      return new ObjectMapper();
   }
}
