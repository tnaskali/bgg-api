package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggForumV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggForumV2Repository {

  private final WebClient webClient;

  public BggForumV2Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v2.forum}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getForum(BggForumV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .doOnNext(entity -> {
              if (MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())) {
                Matcher matcher = Pattern.compile("<div class='messagebox error'>([\\s\\S]*?)</div>").matcher(entity.getBody());
                if (matcher.find()) {
                  String error = matcher.group(1).trim();
                  if ("Object does not exist".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object does not exist");
                  } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error);
                  }
                } else {
                  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
                }
              }
            }
        )
        .map(HttpEntity::getBody);
  }

}
