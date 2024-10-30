package dev.sstol.tasktrackerrestapi.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.AlreadyExistsException;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.BadRequestException;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.InternalServerErrorException;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class RestErrorHandler extends ResponseEntityExceptionHandler {
   private final ObjectMapper mapper;
   private final HttpHeaders httpHeaders;

   @ExceptionHandler(AlreadyExistsException.class)
   public ResponseEntity<String> handleAlreadyExistsException409(AlreadyExistsException ex) {
      try {
         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.CONFLICT); //409
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException(e);
      }
   }

   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
      try {
         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.NOT_FOUND); // 404
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException(e);
      }
   }

   @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class})
   public ResponseEntity<String> handleBadRequestException(RuntimeException ex) {
      try {
         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.BAD_REQUEST); // 400
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException(e);
      }
   }

   @ExceptionHandler(BadCredentialsException.class)
   public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
      try {
         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.UNAUTHORIZED);
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException(e);
      }
   }

   @ExceptionHandler(Throwable.class)
   public ResponseEntity<Void> handleAllOtherExceptions(Throwable ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
   }
}