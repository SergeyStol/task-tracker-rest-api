package dev.sstol.tasktrackerrestapi.infrastructure.metrics;

import dev.sstol.tasktrackerrestapi.features.tasks.TaskService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sergey Stol
 * 2024-11-25
 */
@Component
@RequiredArgsConstructor
public class TaskMetrics {
   private final MeterRegistry meterRegistry;
   private final AtomicLong tasksCounter = new AtomicLong(0);
   private final TaskService taskService;

   @PostConstruct
   void init() {
      Gauge.builder("number_of_tasks", tasksCounter, AtomicLong::get)
        .description("Total number of tasks")
        .register(meterRegistry);
   }

   @Scheduled(fixedRate = 5000)
   void updateTaskCounter() {
      tasksCounter.set(taskService.count());
   }
}
