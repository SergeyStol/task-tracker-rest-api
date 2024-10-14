package dev.sstol.tasktrackerrestapi.features.tasks;

import dev.sstol.tasktrackerrestapi.features.users.User;
import dev.sstol.tasktrackerrestapi.infrastructure.api.BadRequestException400;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskRestController {
   private final TaskService service;

   @GetMapping
   List<TaskDto> getTasks(Authentication authentication) {
      User principal = (User) authentication.getPrincipal();
      return service.findAllByOwnerId(principal.getId());
   }

   @GetMapping("/{id}")
   TaskDto getTask(@PathVariable Long id) {
      return service.findById(id);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   TaskDto addTask(@RequestBody NewTaskDto newTaskDto, Authentication authentication) {
      if (Strings.isBlank(newTaskDto.title())) {
         throw new BadRequestException400("You should specify title");
      }
      User user = (User) authentication.getPrincipal();
      return service.addTask(newTaskDto, user.getId());
   }

   @PutMapping
   @ResponseStatus(HttpStatus.ACCEPTED)

   TaskDto updateTask(@RequestBody TaskDto taskDto) {
      if (taskDto.id() == null) {
         throw new BadRequestException400("You need to specify id");
      }
      return service.updateTask(taskDto);
   }

   @PatchMapping
   @ResponseStatus(HttpStatus.ACCEPTED)
   TaskDto updateCompletedStatus(@RequestBody TaskDto taskDto, Authentication authentication) {
      User principal = (User) authentication.getPrincipal();
      return service.updateCompletedField(principal.getId(), taskDto.id(), taskDto.completed());
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   void delete(@PathVariable Long id, Authentication authentication) {
      Long principalId = ((User) authentication.getPrincipal()).getId();
      service.delete(id, principalId);
   }

}

