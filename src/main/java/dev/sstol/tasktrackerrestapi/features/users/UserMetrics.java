package dev.sstol.tasktrackerrestapi.features.users;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sergey Stol
 * 2024-11-05
 */
@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class UserMetrics {
   private final MeterRegistry meterRegistry;
   private final AtomicLong userCount = new AtomicLong(0);
   private Counter newUserCounter;
   private final UserService userService;

   @PostConstruct
   public void init() {
      Gauge.builder("number_of_users_total", userCount, AtomicLong::get)
        .description("Total number of registered users")
        .register(meterRegistry);

      this.newUserCounter = Counter.builder("add_new_user_counter")
        .description("Counts the number of new user registrations")
        .register(meterRegistry);
   }

   @Scheduled(fixedRate = 5000)
   public void updateUserCount() {
      userCount.set(userService.count());
   }

   @Async
   @EventListener
   public void addNewUserHandler(AddNewUserEvent addNewUserEvent) {
      log.info("Send notification to Prometheus -> Add one more user");
      newUserCounter.increment();
   }

}
