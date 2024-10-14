package dev.sstol.tasktrackerrestapi.infrastructure.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException500 extends RuntimeException {
   public InternalServerErrorException500(Throwable cause) {
      super(cause);
   }
}
