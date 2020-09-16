package li.naska.bgg.exception;

import li.naska.bgg.exception.model.BggApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;

@ControllerAdvice
public class BggApiGlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BggApiError> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
    if (ex instanceof IllegalArgumentException || ex instanceof BggBadRequestError) {
      return respondWith(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    } else if (ex instanceof BggResourceNotFoundError) {
      return respondWith(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    } else if (ex instanceof BggNoContentError) {
      return respondWith(HttpStatus.NO_CONTENT, ex.getMessage(), request.getRequestURI());
    } else if (ex instanceof BggAuthenticationRequiredError) {
      return respondWith(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    } else if (ex instanceof BggResourceNotOwnedError) {
      return respondWith(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI());
    } else if (ex instanceof IllegalStateException || ex instanceof BggUnexpectedResponseException) {
      return respondWith(HttpStatus.EXPECTATION_FAILED, ex.getMessage(), request.getRequestURI());
    } else {
      return respondWith(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI());
    }
  }

  private ResponseEntity<BggApiError> respondWith(HttpStatus httpStatus, String message, String path) {
    BggApiError body = new BggApiError();
    body.setMessage(message);
    body.setTimestamp(ZonedDateTime.now());
    body.setPath(path);
    body.setStatus(httpStatus.value());
    body.setError(httpStatus.getReasonPhrase());
    return ResponseEntity.status(httpStatus).body(body);
  }

}
