package dev.sstol.tasktrackerrestapi.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException400 extends RuntimeException {
   public BadRequestException400(String message) {
      super(message);
   }
}
