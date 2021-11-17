package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggGuildParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggGuildRepository {

  private final WebClient webClient;

  public BggGuildRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.guild}") String guildEndpoint) {
    this.webClient = builder.baseUrl(guildEndpoint).build();
  }

  public Mono<String> getGuild(BggGuildParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(body -> {
          if (body.matches("^<\\?xml version=\"1.0\" encoding=\"utf-8\"\\?><guild id=\".*\"  termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n<error>Guild not found\\.</error>\n</guild>\n$")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guild not found");
          }
        });
  }

}
