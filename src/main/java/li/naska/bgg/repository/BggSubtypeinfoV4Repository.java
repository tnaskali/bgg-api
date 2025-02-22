package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggSubtypeinfoV4QueryParams;
import li.naska.bgg.repository.model.BggSubtypeinfoV4ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class BggSubtypeinfoV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggSubtypeinfoV4Repository(
      @Value("${bgg.endpoints.v4.subtypeinfo}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggSubtypeinfoV4ResponseBody> getSubtypeinfo(BggSubtypeinfoV4QueryParams params) {
    return getSubtypeinfoAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggSubtypeinfoV4ResponseBody.class));
  }

  public Mono<String> getSubtypeinfoAsJson(BggSubtypeinfoV4QueryParams params) {
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
}
