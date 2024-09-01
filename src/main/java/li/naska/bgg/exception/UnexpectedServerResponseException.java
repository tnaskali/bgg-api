package li.naska.bgg.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
public class UnexpectedServerResponseException extends ResponseStatusException {

  private static final String REASON = "Unexpected server response";

  private UnexpectedServerResponseException(String reason) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
  }

  public static StatusAndBodyBuilder from(HttpStatusCode status, String body) {
    return new StatusAndBodyBuilder(status, body);
  }

  public static ClientResponseBuilder from(ClientResponse response) {
    return new ClientResponseBuilder(response);
  }

  public static class StatusAndBodyBuilder {
    private final HttpStatusCode status;
    private final String body;
    private Throwable cause;

    public StatusAndBodyBuilder(HttpStatusCode status, String body) {
      this.status = status;
      this.body = body;
    }

    public StatusAndBodyBuilder withCause(Throwable cause) {
      this.cause = cause;
      return this;
    }

    public UnexpectedServerResponseException build() {
      String logMessage =
          String.format("%s : status='%s', body='%s'", REASON, status.value(), body);
      if (cause != null) {
        log.error(logMessage, cause);
      } else {
        log.error(logMessage);
      }
      return new UnexpectedServerResponseException(REASON);
    }

    public <A> A buildAndThrow() {
      throw build();
    }
  }

  public static class ClientResponseBuilder {
    private final ClientResponse clientResponse;

    public ClientResponseBuilder(ClientResponse clientResponse) {
      this.clientResponse = clientResponse;
    }

    public Mono<UnexpectedServerResponseException> build() {
      return clientResponse.bodyToMono(String.class).defaultIfEmpty("").map(body -> {
        String logMessage =
            String.format("%s : status='%s', body='%s'", REASON, clientResponse.statusCode(), body);
        log.error(logMessage);
        return new UnexpectedServerResponseException(REASON);
      });
    }

    public <A> Mono<A> buildAndThrow() {
      return build().handle((exception, sink) -> {
        sink.error(exception);
      });
    }
  }
}
