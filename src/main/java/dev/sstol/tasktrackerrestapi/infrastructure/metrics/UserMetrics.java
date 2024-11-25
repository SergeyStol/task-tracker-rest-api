package dev.sstol.tasktrackerrestapi.infrastructure.metrics;

import dev.sstol.tasktrackerrestapi.features.users.UserService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sergey Stol
 * 2024-11-05
 */
@Component
@RequiredArgsConstructor
public class UserMetrics {
   private final MeterRegistry meterRegistry;
   private final AtomicLong userCount = new AtomicLong(0);
   private final UserService userService;

   @PostConstruct
   public void init() {
      Gauge.builder("number_of_users_total", userCount, AtomicLong::get)
        .description("Total number of registered users")
        .register(meterRegistry);
   }

   @Scheduled(fixedRate = 5000)
   public void updateUserCount() {
      userCount.set(userService.count());
   }

}
