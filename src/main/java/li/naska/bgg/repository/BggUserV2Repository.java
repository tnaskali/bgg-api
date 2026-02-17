package li.naska.bgg.repository;

import com.boardgamegeek.xml.user.v2.User;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
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
public class BggUserV2Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggUserV2Repository(
      @Value("${bgg.endpoints.v2.user}") String endpoint,
      @Value("${bgg.application.token:UNDEFINED}") String applicationToken,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder
        .baseUrl(endpoint)
        .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", applicationToken))
        .build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getUserAsJson(BggUserV2QueryParams params) {
    return getUser(params).map(xmlProcessor::toJsonString);
  }

  public Mono<User> getUser(BggUserV2QueryParams params) {
    return getUserAsXml(params).map(xml -> xmlProcessor.toJavaObject(xml, User.class));
  }

  public Mono<String> getUserAsXml(BggUserV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND
              || (clientResponse.statusCode() == HttpStatus.OK
                  && clientResponse
                      .headers()
                      .contentType()
                      .filter(MediaType.TEXT_HTML::equalsTypeAndSubtype)
                      .isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_XML::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }
}
