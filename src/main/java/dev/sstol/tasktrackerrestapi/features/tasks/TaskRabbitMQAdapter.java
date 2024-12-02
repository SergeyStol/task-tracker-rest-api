package dev.sstol.tasktrackerrestapi.features.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.sql.Timestamp;

import static dev.sstol.tasktrackerrestapi.features.tasks.TaskRoutingMessageTypes.TASK_CREATED_QUEUE_NAME;
import static dev.sstol.tasktrackerrestapi.infrastructure.rabbitmq.RabbitMQConfig.EXCHANGE_NAME;

/**
 * @author Sergey Stol
 * 2024-12-02
 */
@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class TaskRabbitMQAdapter {
   private final RabbitTemplate rabbitTemplate;
   private final ObjectMapper objectMapper;

   @Async
   @TransactionalEventListener
   void addNewTaskHandler(AddNewTaskEvent addNewTaskEvent) {
      log.info("Push info to --> RabbitMQ. A new task had been created.");
      Task createdTask = addNewTaskEvent.getCreatedTask();
      var taskSchedulerDto = new TaskSchedulerDto(createdTask.getId(),
        createdTask.getOwner().getEmail(), createdTask.getTitle(), createdTask.getCompletedDate());
      try {
         rabbitTemplate.convertAndSend(EXCHANGE_NAME, TASK_CREATED_QUEUE_NAME,
           objectMapper.writeValueAsString(taskSchedulerDto));
      } catch (JsonProcessingException e) {
         log.error("Can't publish new task info to rabbitMQ. Reason: {}", e.getMessage());
         throw new RuntimeException(e);
      }
   }

   record TaskSchedulerDto(Long id, String owner, String title, Timestamp completedDate){}
}
