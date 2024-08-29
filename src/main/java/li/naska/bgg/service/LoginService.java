package li.naska.bgg.service;

import com.jayway.jsonpath.JsonPath;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class LoginService {

  private final WebClient webClient;

  public LoginService(
      WebClient.Builder builder, @Value("${bgg.endpoints.v5.login}") String loginEndpoint) {
    webClient = builder.baseUrl(loginEndpoint).build();
  }

  @Cacheable(cacheNames = "login", key = "#username")
  public Mono<List<String>> login(String username, String password) {
    Map<String, Object> credentials = new HashMap<>();
    credentials.put("username", username);
    credentials.put("password", password);
    Map<String, Object> formData = new HashMap<>();
    formData.put("credentials", credentials);

    return webClient
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(formData)
        .retrieve()
        .onStatus(status -> status != HttpStatus.NO_CONTENT, response -> response
            .bodyToMono(String.class)
            .map(body -> JsonPath.<String>read(body, "$.errors.message"))
            .map(body -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, body)))
        .toEntity(Void.class)
        .map(response -> Optional.ofNullable(response.getHeaders().get(HttpHeaders.SET_COOKIE))
            .orElse(Collections.emptyList()));
  }
}
