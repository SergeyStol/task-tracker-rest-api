package dev.sstol.tasktrackerrestapi.infrastructure.exceptions;

/**
 * @author Sergey Stol
 * 2024-10-22
 */
public class NotFoundException extends RuntimeException {
   public NotFoundException(String message) {
      super(message);
   }
}
