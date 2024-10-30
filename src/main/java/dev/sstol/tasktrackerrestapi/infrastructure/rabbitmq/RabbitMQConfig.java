package dev.sstol.tasktrackerrestapi.infrastructure.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Stol
 * 2024-10-12
 */
@Configuration
public class RabbitMQConfig {

   public static final String EXCHANGE_NAME = "TASKS_TRACKER_EXCHANGE";

   @Bean
   public DirectExchange directExchange() {
      return new DirectExchange(EXCHANGE_NAME);
   }

   @Bean
   public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
      RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
      rabbitTemplate.setMessageConverter(jsonMessageConverter());
      return rabbitTemplate;
   }

   @Bean
   public Jackson2JsonMessageConverter jsonMessageConverter() {
      return new Jackson2JsonMessageConverter();
   }
}
