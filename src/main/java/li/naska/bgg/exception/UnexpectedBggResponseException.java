package li.naska.bgg.exception;

import java.net.URI;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class UnexpectedBggResponseException extends ResponseStatusException {

  private final MediaType bggResponseContentType;
  private final HttpStatusCode bggResponseStatusCode;
  private final URI bggRequestUri;

  public UnexpectedBggResponseException(ClientResponse clientResponse) {
    this(clientResponse, HttpStatus.INTERNAL_SERVER_ERROR, null);
  }

  public UnexpectedBggResponseException(ClientResponse clientResponse, HttpStatus status) {
    this(clientResponse, status, null);
  }

  public UnexpectedBggResponseException(ClientResponse clientResponse, String message) {
    this(clientResponse, HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public UnexpectedBggResponseException(
      ClientResponse clientResponse, HttpStatus status, String message) {
    super(status, message);
    this.bggRequestUri = clientResponse.request().getURI();
    this.bggResponseStatusCode = clientResponse.statusCode();
    this.bggResponseContentType = clientResponse.headers().contentType().orElse(null);
  }
}
