package dev.sstol.tasktrackerrestapi.features.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import static dev.sstol.tasktrackerrestapi.infrastructure.rabbitmq.RabbitMQConfig.EXCHANGE_NAME;

/**
 * @author Sergey Stol
 * 2024-12-02
 */
@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class UserRabbitMQAdapter {
   private final RabbitTemplate rabbitTemplate;
   private final ObjectMapper objectMapper;

   @Async
   @EventListener
   void addNewUserHandler(AddNewUserEvent addNewUserEvent) {
      log.info("Push notification to RabbitMQ -> AddNewUserEvent");
      try {
         rabbitTemplate.convertAndSend(EXCHANGE_NAME, "USER_CREATED",
           objectMapper.writeValueAsString(addNewUserEvent.getUserDto()));
      } catch (JsonProcessingException e) {
         log.error("Can't publish new task info to rabbitMQ. Reason: {}", e.getMessage());
         throw new RuntimeException(e);
      }
   }
}
