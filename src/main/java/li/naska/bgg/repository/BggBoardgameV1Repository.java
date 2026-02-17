package li.naska.bgg.repository;

import com.boardgamegeek.xml.boardgame.v1.Boardgames;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggBoardgameV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggBoardgameV1Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggBoardgameV1Repository(
      @Value("${bgg.endpoints.v1.boardgame}") String endpoint,
      @Value("${bgg.application.token:UNDEFINED}") String applicationToken,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder
        .baseUrl(endpoint)
        .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", applicationToken))
        .build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getBoardgamesAsJson(Set<Integer> ids, BggBoardgameV1QueryParams params) {
    return getBoardgames(ids, params).map(xmlProcessor::toJsonString);
  }

  public Mono<Boardgames> getBoardgames(Set<Integer> ids, BggBoardgameV1QueryParams params) {
    return getBoardgamesAsXml(ids, params)
        .map(xml -> xmlProcessor.toJavaObject(xml, Boardgames.class));
  }

  public Mono<String> getBoardgamesAsXml(Set<Integer> ids, BggBoardgameV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{ids}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(ids.stream().map(Objects::toString).collect(Collectors.joining(","))))
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
        .doOnNext(body -> xmlProcessor
            .xPathValue(body, "/boardgames/boardgame/error/@message")
            .ifPresent(message -> {
              if (message.equals("Item not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
              } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
              }
            }));
  }
}
