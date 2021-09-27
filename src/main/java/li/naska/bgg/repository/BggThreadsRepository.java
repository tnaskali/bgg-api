package li.naska.bgg.repository;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.repository.model.BggThreadsParameters;
import li.naska.bgg.util.QueryParamFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Repository
public class BggThreadsRepository {

  private final WebClient webClient;

  public BggThreadsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thread.read}") String threadReadEndpoint) {
    this.webClient = builder.baseUrl(threadReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractThreadsParams(BggThreadsParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", params.getId().toString());
    Optional.ofNullable(params.getMinarticleid()).map(Object::toString).ifPresent(e -> map.set("minarticleid", e));
    if (params.getMinarticledate() != null) {
      if (params.getMinarticletime() != null) {
        map.set("minarticledate", QueryParamFunctions.BGG_LOCALDATETIME_FUNCTION.apply(params.getMinarticledate().atTime(params.getMinarticletime())));
      } else {
        map.set("minarticledate", QueryParamFunctions.BGG_LOCALDATE_FUNCTION.apply(params.getMinarticledate()));
      }
    }
    Optional.ofNullable(params.getCount()).map(Object::toString).ifPresent(e -> map.set("count", e));
    // BGG API: not currently supported
    // Optional.ofNullable(params.getUsername()).ifPresent(e -> map.set("username", e));
    return map;
  }

  public Mono<Thread> getThread(BggThreadsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractThreadsParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Thread.class);
  }

}
