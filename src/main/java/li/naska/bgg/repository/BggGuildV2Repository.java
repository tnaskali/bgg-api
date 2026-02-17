package li.naska.bgg.repository;

import com.boardgamegeek.xml.guild.v2.Guild;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGuildV2QueryParams;
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
public class BggGuildV2Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggGuildV2Repository(
      @Value("${bgg.endpoints.v2.guild}") String endpoint,
      @Value("${bgg.application.token:UNDEFINED}") String applicationToken,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder
        .baseUrl(endpoint)
        .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", applicationToken))
        .build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getGuildAsJson(BggGuildV2QueryParams params) {
    return getGuild(params).map(xmlProcessor::toJsonString);
  }

  public Mono<Guild> getGuild(BggGuildV2QueryParams params) {
    return getGuildAsXml(params).map(xml -> xmlProcessor.toJavaObject(xml, Guild.class));
  }

  public Mono<String> getGuildAsXml(BggGuildV2QueryParams params) {
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
