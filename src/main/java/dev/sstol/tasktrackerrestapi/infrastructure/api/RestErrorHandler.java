package dev.sstol.tasktrackerrestapi.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sstol.tasktrackerrestapi.infrastructure.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class RestErrorHandler extends ResponseEntityExceptionHandler {
   private final ObjectMapper mapper;
   private final HttpHeaders httpHeaders;

   @ExceptionHandler(AlreadyExistsException409.class)
   public ResponseEntity<String> handleAlreadyExistsException409(AlreadyExistsException409 ex) {
      try {
         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.CONFLICT);
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException500(e);
      }
   }

   @ExceptionHandler(BadRequestException400.class)
   public ResponseEntity<String> handleBadRequestException409(BadRequestException400 ex) {
      try {

         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.BAD_REQUEST);
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException500(e);
      }
   }

   @ExceptionHandler(NotFoundException.class)
   public ResponseEntity<String> handleBadRequestException409(NotFoundException ex) {
      try {
         return new ResponseEntity<>(
           mapper.writeValueAsString(Map.of("message", ex.getMessage())),
           httpHeaders,
           HttpStatus.NOT_FOUND);
      } catch (JsonProcessingException e) {
         throw new InternalServerErrorException500(e);
      }
   }
}