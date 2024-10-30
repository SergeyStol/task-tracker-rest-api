package dev.sstol.tasktrackerrestapi.infrastructure.exceptions;

/**
 * @author Sergey Stol
 * 2024-10-08
 */
public class InternalServerErrorException extends RuntimeException {
   public InternalServerErrorException(Throwable cause) {
      super(cause);
   }
}
