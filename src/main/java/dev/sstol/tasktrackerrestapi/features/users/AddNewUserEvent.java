package dev.sstol.tasktrackerrestapi.features.users;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Sergey Stol
 * 2024-12-02
 */
public class AddNewUserEvent extends ApplicationEvent {
   @Getter
   private final UserDto userDto;
   public AddNewUserEvent(Object source, UserDto userDto) {
      super(source);
      this.userDto = userDto;
   }
}
