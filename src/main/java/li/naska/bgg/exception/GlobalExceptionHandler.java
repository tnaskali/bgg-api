package li.naska.bgg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
    if (ex instanceof IllegalArgumentException) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("{ \"error\": \"%s\" }", ex.getMessage()));
    } else if (ex instanceof BggBadRequestError) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("{ \"error\": \"%s\" }", ex.getMessage()));
    } else if (ex instanceof BggResourceNotFoundError) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("{ \"error\": \"%s\" }", ex.getMessage()));
    } else if (ex instanceof BggNoContentError) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("{ \"error\": \"%s\" }", ex.getMessage()));
    } else if (ex instanceof BggAuthenticationRequiredError) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(String.format("{ \"error\": \"%s\" }", ex.getMessage()));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("{ \"error\": \"%s\" }", ex.getMessage()));
  }

}
