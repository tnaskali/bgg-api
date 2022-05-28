package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggThreadQueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Threads
 * <p>
 * With the XMLAPI2 you can request forum threads by thread id. A thread consists of some basic information about the
 * thread and a series of articles or individual postings.
 * <p>
 * Base URI: /xmlapi2/thread?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc7">BGG_XML_API2</a>
 */
@Repository
public class BggThreadsRepository {

  private final WebClient webClient;

  public BggThreadsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thread}") String threadEndpoint) {
    this.webClient = builder.baseUrl(threadEndpoint).build();
  }

  public Mono<String> getThread(BggThreadQueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException))
        .doOnNext(body -> {
          if (body.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><error message='Thread Not Found' />")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
          }
        });
  }

}
