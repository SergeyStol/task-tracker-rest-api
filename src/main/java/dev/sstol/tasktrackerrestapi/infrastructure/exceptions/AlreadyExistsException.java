package dev.sstol.tasktrackerrestapi.infrastructure.exceptions;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
public class AlreadyExistsException extends RuntimeException {
   public AlreadyExistsException(String message, Throwable cause) {
      super(message, cause);
   }
}
