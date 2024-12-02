package dev.sstol.tasktrackerrestapi.features.tasks;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Sergey Stol
 * 2024-12-02
 */
@Getter
public class AddNewTaskEvent extends ApplicationEvent {
   private final Task createdTask;
   public AddNewTaskEvent(Object source, Task createdTask) {
      super(source);
      this.createdTask = createdTask;
   }
}
