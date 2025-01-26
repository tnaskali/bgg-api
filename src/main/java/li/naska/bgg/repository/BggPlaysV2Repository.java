package li.naska.bgg.repository;

import com.boardgamegeek.plays.v2.Plays;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggPlaysV2Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggPlaysV2Repository(
      @Value("${bgg.endpoints.v2.plays}") String endpoint,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getPlaysAsJson(BggPlaysV2QueryParams params) {
    return getPlays(params).map(xmlProcessor::toJsonString);
  }

  public Mono<Plays> getPlays(BggPlaysV2QueryParams params) {
    return getPlaysAsXml(params).map(xml -> xmlProcessor.toJavaObject(xml, Plays.class));
  }

  public Mono<String> getPlaysAsXml(BggPlaysV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_XML::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        })
        .doOnNext(body -> xmlProcessor.xPathValue(body, "/error/@message").ifPresent(message -> {
          if (message.equals("Not Found")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
          }
        }));
  }
}
