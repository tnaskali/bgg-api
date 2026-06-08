package li.naska.bgg.exception;

import java.net.URI;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BggAuthorizationException extends ResponseStatusException {

  private final MediaType bggResponseContentType;
  private final HttpStatusCode bggResponseStatusCode;
  private final URI bggRequestUri;

  public BggAuthorizationException(ClientResponse clientResponse) {
    this(clientResponse, HttpStatus.UNAUTHORIZED, null);
  }

  public BggAuthorizationException(ClientResponse clientResponse, String message) {
    this(clientResponse, HttpStatus.UNAUTHORIZED, message);
  }

  public BggAuthorizationException(
      ClientResponse clientResponse, HttpStatus status, String message) {
    super(status, message);
    this.bggRequestUri = clientResponse.request().getURI();
    this.bggResponseStatusCode = clientResponse.statusCode();
    this.bggResponseContentType = clientResponse.headers().contentType().orElse(null);
  }
}
