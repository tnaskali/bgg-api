package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggGuildV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGuildV2Repository {

  private final WebClient webClient;

  public BggGuildV2Repository(
      @Autowired WebClient.Builder builder, @Value("${bgg.endpoints.v2.guild}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getGuild(BggGuildV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(body -> {
          if (body.matches(
              "^<\\?xml version=\"1.0\" encoding=\"utf-8\"\\?><guild id=\".*\"  termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n<error>Guild not found\\.</error>\n</guild>\n$")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guild not found");
          }
        });
  }
}
