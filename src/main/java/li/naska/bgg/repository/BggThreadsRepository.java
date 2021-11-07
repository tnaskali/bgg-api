package li.naska.bgg.repository;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.repository.model.BggThreadsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggThreadsRepository {

  private final WebClient webClient;

  public BggThreadsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thread}") String threadEndpoint) {
    this.webClient = builder.baseUrl(threadEndpoint).build();
  }

  public Mono<Thread> getThread(BggThreadsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Thread.class);
  }

}
