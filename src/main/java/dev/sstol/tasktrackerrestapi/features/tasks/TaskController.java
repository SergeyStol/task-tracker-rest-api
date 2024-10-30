package dev.sstol.tasktrackerrestapi.features.tasks;

import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
   private final TaskService service;

   @GetMapping
   @ResponseStatus(HttpStatus.OK)
   List<TaskDto> getTasks(Authentication authentication) {
      User principal = (User) authentication.getPrincipal();
      return service.findAllByOwnerId(principal.getId());
   }

   @GetMapping("/{id}")
   @ResponseStatus(HttpStatus.OK)
   TaskDto getTask(@PathVariable Long id, Authentication authentication) {
      User principal = (User) authentication.getPrincipal();
      return service.findById(id, principal.getId());
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   TaskDto addTask(@RequestBody NewTaskDto newTaskDto, Authentication authentication) {
      if (Strings.isBlank(newTaskDto.title())) {
         throw new BadRequestException("You should specify title");
      }
      User user = (User) authentication.getPrincipal();
      return service.addTask(newTaskDto, user.getId());
   }

   @PutMapping
   @ResponseStatus(HttpStatus.ACCEPTED)
   TaskDto updateTask(@RequestBody TaskDto taskDto) {
      if (taskDto.id() == null) {
         throw new BadRequestException("You need to specify id");
      }
      return service.updateTask(taskDto);
   }

   @PatchMapping("/{id}")
   @ResponseStatus(HttpStatus.ACCEPTED)
   TaskDto patchTask(@PathVariable Long id,
                     @RequestBody TaskDto taskDto,
                     Authentication authentication) {
      if (!id.equals(taskDto.id())) {
         throw new BadRequestException("Task id in the path isn't equal task id in the request body");
      }
      User principal = (User) authentication.getPrincipal();
      return service.patchTask(taskDto, principal.getId());
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   void delete(@PathVariable Long id, Authentication authentication) {
      User principal = (User) authentication.getPrincipal();
      log.info("User email={} userId={}: Delete task taskId={}", principal.getEmail(), principal.getId(), id);
      service.delete(id, principal.getId());
   }

}

