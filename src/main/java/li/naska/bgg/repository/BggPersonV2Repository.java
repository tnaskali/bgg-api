package li.naska.bgg.repository;

import com.boardgamegeek.person.v2.Items;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggPersonV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggPersonV2Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggPersonV2Repository(
      @Value("${bgg.endpoints.v2.person}") String endpoint,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getItemsAsJson(BggPersonV2QueryParams params) {
    return getItems(params).map(xmlProcessor::toJsonString);
  }

  public Mono<Items> getItems(BggPersonV2QueryParams params) {
    return getItemsAsXml(params).map(xml -> xmlProcessor.toJavaObject(xml, Items.class));
  }

  public Mono<String> getItemsAsXml(BggPersonV2QueryParams params) {
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
        });
  }
}
