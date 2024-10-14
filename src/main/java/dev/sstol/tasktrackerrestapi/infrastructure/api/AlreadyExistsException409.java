package dev.sstol.tasktrackerrestapi.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException409 extends RuntimeException {
   public AlreadyExistsException409(String message, Throwable cause) {
      super(message, cause);
   }
}
