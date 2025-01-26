package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGeekcollectionV3QueryParams;
import li.naska.bgg.util.HtmlProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Repository
public class BggGeekcollectionV3Repository {

  private final WebClient webClient;

  private final HtmlProcessor htmlProcessor;

  public BggGeekcollectionV3Repository(
      @Value("${bgg.endpoints.v3.geekcollection}") String endpoint,
      WebClient.Builder builder,
      HtmlProcessor htmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.htmlProcessor = htmlProcessor;
  }

  public Mono<String> getGeekcollectionAsCsv(
      Optional<String> cookie, BggGeekcollectionV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.valueOf("text/comma-separated-values"))
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.OK
              && clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_HTML::equalsTypeAndSubtype)
                  .isPresent()) {
            return clientResponse
                .bodyToMono(String.class)
                .map(htmlProcessor::parse)
                .handle((doc, sink) -> {
                  Optional<String> error =
                      htmlProcessor.getFirstElementTextByClass(doc, "messagebox error");
                  if (error.isPresent()) {
                    if ("Your request for this collection has been accepted and will be processed."
                        .equals(error.get())) {
                      sink.error(new BggResponseNotReadyException());
                    } else if ("Invalid username specified".equals(error.get())) {
                      sink.error(
                          new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username"));
                    } else {
                      sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                    }
                  } else {
                    sink.error(new UnexpectedBggResponseException(clientResponse));
                  }
                });
          } else if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.valueOf("text/comma-separated-values")::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        })
        .retryWhen(Retry.backoff(4, Duration.ofSeconds(4))
            .filter(BggResponseNotReadyException.class::isInstance));
  }
}
