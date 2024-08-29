package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import li.naska.bgg.repository.model.BggGeekitempollV3QueryParams;
import li.naska.bgg.repository.model.BggGeekitempollV3ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekitempollV3Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggGeekitempollV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekpollitem}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggGeekitempollV3ResponseBody> getGeekitempoll(
      Optional<String> cookie, BggGeekitempollV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .retrieve()
        .toEntity(String.class)
        .doOnNext(entity -> {
          Matcher matcher = Pattern.compile("<div class='messagebox error'>([\\s\\S]*?)</div>")
              .matcher(entity.getBody());
          if (matcher.find()) {
            String error = matcher.group(1).trim();
            if ("invalid poll args".equals(error)) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid arguments");
            }
          }
        })
        .handle((entity, sink) -> {
          try {
            sink.next(
                objectMapper.readValue(entity.getBody(), BggGeekitempollV3ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }
}
