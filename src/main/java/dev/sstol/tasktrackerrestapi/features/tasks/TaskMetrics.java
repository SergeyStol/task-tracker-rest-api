package dev.sstol.tasktrackerrestapi.features.tasks;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Sergey Stol
 * 2024-11-25
 */
@Slf4j
@Component
@EnableAsync
@RequiredArgsConstructor
public class TaskMetrics {
   private final MeterRegistry meterRegistry;
   private final TaskService taskService;
   private Counter numberOfCreatedTasks;

   private final AtomicLong tasksGuage = new AtomicLong(0);
   private final AtomicLong openedTasksCounter = new AtomicLong(0);
   private final AtomicLong completedTasksCounter = new AtomicLong(0);

   @PostConstruct
   void init() {
      Gauge.builder("number_of_tasks", tasksGuage, AtomicLong::get)
        .description("Total number of tasks")
        .register(meterRegistry);

      numberOfCreatedTasks = Counter.builder("tasks_created")
        .description("Number of created tasks")
        .register(meterRegistry);

      Gauge.builder("number_of_opened_tasks", openedTasksCounter, AtomicLong::get)
        .description("Number of created tasks")
        .register(meterRegistry);

      Gauge.builder("number_of_completed_tasks", completedTasksCounter, AtomicLong::get)
        .description("Number of completed tasks")
        .register(meterRegistry);
   }

   @Scheduled(fixedRate = 5000)
   void updateTaskCounter() {
      tasksGuage.set(taskService.totalTasksCount());
   }

   @Scheduled(fixedRate = 5000)
   void updateOpenedTaskCounter() {
      long openTasksNumber = taskService.totalOpenedTasksCount();
      log.info("Sending number of opened tasks: {}", openedTasksCounter);
      openedTasksCounter.set(openTasksNumber);
   }

   @Scheduled(fixedRate = 5000)
   void updateCompletedTaskCounter() {
      long completedTasksNumber = taskService.totalCompletedTasksCount();
      log.info("Sending number of completed tasks: {}", completedTasksNumber);
      completedTasksCounter.set(completedTasksNumber);
   }

   @Async
   @TransactionalEventListener
   public void countCreatedTask(AddNewTaskEvent addNewTaskEvent) {
      log.info("Push info to --> Prometheus. A new task had been created.");
      numberOfCreatedTasks.increment();
   }
}
