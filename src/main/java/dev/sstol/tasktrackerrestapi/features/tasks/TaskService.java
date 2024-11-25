package dev.sstol.tasktrackerrestapi.features.tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static dev.sstol.tasktrackerrestapi.features.tasks.TaskRoutingMessageTypes.TASK_CREATED_QUEUE_NAME;
import static dev.sstol.tasktrackerrestapi.features.tasks.TaskRoutingMessageTypes.TASK_UPDATED_QUEUE_NAME;
import static dev.sstol.tasktrackerrestapi.infrastructure.rabbitmq.RabbitMQConfig.EXCHANGE_NAME;

/**
 * @author Sergey Stol
 * 2024-10-10
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

   private final TaskRepository repository;
   private final TaskMapper mapper;
   private final EntityManager entityManager;
   private final RabbitTemplate rabbitTemplate;
   private final ObjectMapper objectMapper;

   @Transactional
   public TaskDto addTask(NewTaskDto newTaskDto, Long ownerId) {
      User taskOwner = entityManager.getReference(User.class, ownerId);
      Task newTask = mapper.toEntity(newTaskDto);
      newTask.setOwner(taskOwner);
      Task task = repository.save(newTask);
      notify(newTask);
      return mapper.toDto(task);
   }

   private void notify(Task newTask) {
      record TaskSchedulerDto
         (Long id, String owner, String title, Timestamp completedDate){};
      var taskSchedulerDto = new TaskSchedulerDto(newTask.getId(),
        newTask.getOwner().getEmail(), newTask.getTitle(), newTask.getCompletedDate());
      try {
         rabbitTemplate.convertAndSend(EXCHANGE_NAME, TASK_CREATED_QUEUE_NAME, objectMapper.writeValueAsString(taskSchedulerDto));
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
   }

   public List<TaskDto> findAllByOwnerId(Long id) {
      List<Task> tasks = repository.findAllByOwner_Id(id);
      log.info("Found {} tasks", tasks.size());
      return tasks.stream().map(mapper::toDto).toList();
   }

   public TaskDto findById(Long id, Long ownerId) {
      Task task = repository.findByIdAndOwner_Id(id, ownerId)
        .orElseThrow(() -> new NotFoundException("Task not found"));
      return mapper.toDto(task);
   }

   public TaskDto patchTask(TaskDto taskDto, Long ownerId) {
      if (taskDto == null || taskDto.id() == null) {
         throw new IllegalArgumentException("taskDto or taskDto.id() is null");
      }

      Task task = repository.findByIdAndOwner_Id(taskDto.id(), ownerId)
        .orElseThrow(() -> new NotFoundException("Can't find task with specified id"));

      if (!Strings.isBlank(taskDto.title())) {
         task.setTitle(taskDto.title());
      }

      if (taskDto.description() != null) {
         task.setDescription(taskDto.description());
      }

      if (taskDto.completed() != null) {
         task.setCompleted(taskDto.completed());
         if (taskDto.completed()) {
            task.setCompletedDate(Timestamp.from(Instant.now()));
         }
      }

      if (taskDto.completedDate() != null) {
         task.setCompletedDate(taskDto.completedDate());
      }

      Task savedTask = repository.save(task);
      return mapper.toDto(savedTask);
   }

   @Transactional
   public TaskDto updateTask(TaskDto taskDto) {
      Task task = repository.findById(taskDto.id())
        .orElseThrow(() -> new NotFoundException("Can't find task with id=" + taskDto.id()));
      task.setTitle(Strings.isBlank(taskDto.title()) ? task.getTitle() : taskDto.title());
      task.setDescription(taskDto.description() == null ? task.getDescription() : taskDto.description());
      task.setCompleted(taskDto.completedDate() != null || task.getCompletedDate() != null);
      task.setCompletedDate(taskDto.completedDate() == null ? task.getCompletedDate() : taskDto.completedDate());

      Task savedTask = repository.save(task);

      TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
         @Override
         public void afterCommit() {
            try {
               record TaskDtoScheduler(Long id, String owner, String title, Timestamp completedDate) {}
               var taskDtoScheduler = new TaskDtoScheduler(task.getId(), task.getOwner().getEmail(), task.getTitle(), task.getCompletedDate());
               String taskMessage = objectMapper.writeValueAsString(taskDtoScheduler);
               rabbitTemplate.convertAndSend(EXCHANGE_NAME, TASK_UPDATED_QUEUE_NAME, taskMessage);
               System.out.println("Task update notification sent: " + taskMessage);
            } catch (JsonProcessingException e) {
               throw new RuntimeException("Error while serializing task to JSON", e);
            }
         }
      });
      return mapper.toDto(savedTask);
   }

   public void delete(Long taskId, Long principalId) {
      repository.deleteTaskByIdAndOwnerId(taskId, principalId);
   }

   public long count() {
      return repository.count();
   }
}
