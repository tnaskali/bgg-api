package li.naska.bgg.repository;

import com.boardgamegeek.guild.Guild;
import li.naska.bgg.repository.model.BggGuildParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggGuildsRepository {

  private final WebClient webClient;

  public BggGuildsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.guild}") String guildEndpoint) {
    this.webClient = builder.baseUrl(guildEndpoint).build();
  }

  public Mono<Guild> getGuild(BggGuildParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Guild.class);
  }

}
