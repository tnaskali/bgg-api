package li.naska.bgg.repository;

import com.boardgamegeek.geeklist.v1.Geeklist;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGeeklistV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Repository
public class BggGeeklistV1Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggGeeklistV1Repository(
      @Value("${bgg.endpoints.v1.geeklist}") String endpoint,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getGeeklistAsJson(Integer id, BggGeeklistV1QueryParams params) {
    return getGeeklist(id, params).map(xmlProcessor::toJsonString);
  }

  public Mono<Geeklist> getGeeklist(Integer id, BggGeeklistV1QueryParams params) {
    return getGeeklistAsXml(id, params).map(xml -> xmlProcessor.toJavaObject(xml, Geeklist.class));
  }

  public Mono<String> getGeeklistAsXml(Integer id, BggGeeklistV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .path("/{id}")
            .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.ACCEPTED) {
            // retry later as BGG might queue the request
            throw new BggResponseNotReadyException();
          } else if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_XML::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        })
        .retryWhen(Retry.backoff(4, Duration.ofSeconds(4))
            .filter(BggResponseNotReadyException.class::isInstance));
  }
}
