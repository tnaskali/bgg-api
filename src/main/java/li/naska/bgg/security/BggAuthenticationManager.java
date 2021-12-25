package li.naska.bgg.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class BggAuthenticationManager implements ReactiveAuthenticationManager {

  private final WebClient webClient;

  public BggAuthenticationManager(WebClient.Builder builder,
                                  @Value("${bgg.endpoints.login}") String loginEndpoint) {
    webClient = builder
        .baseUrl(loginEndpoint)
        .build();
  }

  @Override
  public Mono<Authentication> authenticate(final Authentication authentication) throws AuthenticationException {
    final String username = authentication.getName();
    final String password = authentication.getCredentials().toString();
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
        .onStatus(
            status -> status != HttpStatus.ACCEPTED,
            response -> Mono.error(new AuthenticationServiceException("Remote authentication failed")))
        .toEntity(String.class)
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof IOException))
        .map(response -> new BggAuthenticationToken(
            Optional.ofNullable(response.getHeaders().get(HttpHeaders.SET_COOKIE))
                .flatMap(l -> l.stream().filter(e -> e.startsWith("SessionID")).findFirst())
                .orElse(null),
            Optional.ofNullable(response.getHeaders().get(HttpHeaders.SET_COOKIE))
                .flatMap(l -> l.stream().filter(e -> e.startsWith("bggusername")).findFirst())
                .orElse(null),
            Optional.ofNullable(response.getHeaders().get(HttpHeaders.SET_COOKIE))
                .flatMap(l -> l.stream().filter(e -> e.startsWith("bggpassword")).findFirst())
                .orElse(null)
        ));
  }

}
