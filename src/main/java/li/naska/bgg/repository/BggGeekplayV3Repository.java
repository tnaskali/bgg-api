package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.HtmlProcessor;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekplayV3Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  private final HtmlProcessor htmlProcessor;

  public BggGeekplayV3Repository(
      @Value("${bgg.endpoints.v3.geekplay}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor,
      HtmlProcessor htmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
    this.htmlProcessor = htmlProcessor;
  }

  public Mono<BggGeekplayPlaysV3ResponseBody> getGeekplayPlays(
      BggGeekplayPlaysV3QueryParams params) {
    return getGeekplayPlaysAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekplayPlaysV3ResponseBody.class));
  }

  public Mono<String> getGeekplayPlaysAsJson(BggGeekplayPlaysV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggGeekplayCountV3ResponseBody> getGeekplayCount(
      String cookie, BggGeekplayCountV3QueryParams params) {
    return getGeekplayCountAsJson(cookie, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekplayCountV3ResponseBody.class));
  }

  public Mono<String> getGeekplayCountAsJson(String cookie, BggGeekplayCountV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggGeekplayV3ResponseBody> updateGeekplay(
      String cookie, BggGeekplayV3RequestBody requestBody) {
    return updateGeekplayAsJson(cookie, requestBody)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekplayV3ResponseBody.class))
        .doOnNext(responseBody -> {
          if ("delete".equals(requestBody.getAction())
              && !Boolean.TRUE.equals(responseBody.getSuccess())) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                responseBody.getError() != null ? responseBody.getError() : "Error deleting play");
          } else if (responseBody.getError() != null) {
            if ("You must login to save plays".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication required");
            } else if ("Invalid item. Play not saved.".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item");
            } else {
              throw new ResponseStatusException(
                  HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
            }
          }
        });
  }

  public Mono<String> updateGeekplayAsJson(String cookie, BggGeekplayV3RequestBody requestBody) {
    return webClient
        .post()
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.COOKIE, cookie)
        .bodyValue(requestBody)
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
                    if ("You can't delete this play".equals(error.get())) {
                      sink.error(new ResponseStatusException(
                          HttpStatus.UNAUTHORIZED, "You can't delete this play"));
                    } else if ("Play does not exist.".equals(error.get())) {
                      sink.error(new ResponseStatusException(
                          HttpStatus.BAD_REQUEST, "Play does not exist"));
                    } else if ("Invalid action".equals(error.get())) {
                      sink.error(
                          new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action"));
                    } else if ("You are not permitted to edit this play.".equals(error.get())) {
                      sink.error(new ResponseStatusException(
                          HttpStatus.UNAUTHORIZED, "Operation not allowed"));
                    } else {
                      sink.error(new ResponseStatusException(
                          HttpStatus.INTERNAL_SERVER_ERROR, error.get()));
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
        });
  }
}
