package li.naska.bgg.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class BggAuthenticationProvider implements AuthenticationProvider {

  private final WebClient webClient;

  public BggAuthenticationProvider(WebClient.Builder builder,
                                   @Value("${bgg.endpoints.login.write}") String loginEndpoint) {
    webClient = builder
        .baseUrl(loginEndpoint)
        .build();
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("action", "login");
    formData.add("username", authentication.getName());
    formData.add("password", authentication.getCredentials().toString());

    Mono<List<String>> result = webClient
        .post()
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .bodyValue(formData)
        .retrieve()
        .onStatus(
            status -> status != HttpStatus.OK,
            response -> Mono.error(new AuthenticationServiceException("Remote authentication failed")))
        .toEntity(String.class)
        .map(response -> response.getHeaders().get(HttpHeaders.SET_COOKIE));
    return new BggAuthenticationToken(result.block());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return Authentication.class.isAssignableFrom(authentication);
  }

}
