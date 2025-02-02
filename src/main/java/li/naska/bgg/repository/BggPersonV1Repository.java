package li.naska.bgg.repository;

import com.boardgamegeek.person.v1.People;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggPersonV1Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggPersonV1Repository(
      @Value("${bgg.endpoints.v1.person}") String endpoint,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getPersonsAsJson(Set<Integer> ids) {
    return getPersons(ids).map(xmlProcessor::toJsonString);
  }

  public Mono<People> getPersons(Set<Integer> ids) {
    return getPersonsAsXml(ids).map(xml -> xmlProcessor.toJavaObject(xml, People.class));
  }

  public Mono<String> getPersonsAsXml(Set<Integer> ids) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{ids}")
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
            .xPathValue(body, "/people/person/error/@message")
            .ifPresent(message -> {
              if (message.equals("Item not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
              } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
              }
            }));
  }
}
