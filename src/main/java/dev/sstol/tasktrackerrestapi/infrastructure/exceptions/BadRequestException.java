package dev.sstol.tasktrackerrestapi.infrastructure.exceptions;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
public class BadRequestException extends RuntimeException {
   public BadRequestException(String message) {
      super(message);
   }
}
