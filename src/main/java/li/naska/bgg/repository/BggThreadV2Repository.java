package li.naska.bgg.repository;

import com.boardgamegeek.thread.v2.Thread;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggThreadV2QueryParams;
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
public class BggThreadV2Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggThreadV2Repository(
      @Value("${bgg.endpoints.v2.thread}") String endpoint,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getThreadAsJson(BggThreadV2QueryParams params) {
    return getThread(params).map(xmlProcessor::toJsonString);
  }

  public Mono<Thread> getThread(BggThreadV2QueryParams params) {
    return getThreadAsXml(params).map(xml -> xmlProcessor.toJavaObject(xml, Thread.class));
  }

  public Mono<String> getThreadAsXml(BggThreadV2QueryParams params) {
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
          if (message.equals("Thread Not Found")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
          } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
          }
        }));
  }
}
