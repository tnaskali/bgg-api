package li.naska.bgg.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class BggAuthenticationProvider implements AuthenticationProvider {

  @Value("${bgg.endpoints.login.write}")
  private String loginEndpoint;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("username", username);
    formData.add("password", password);

    WebClient webClient = WebClient.builder().build();
    ResponseEntity<String> response = webClient.post()
            .uri(loginEndpoint)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(formData)
            .exchangeToMono(c -> c.toEntity(String.class))
            .block();
    if (response.getStatusCode() != HttpStatus.OK) {
      throw new AuthenticationServiceException("Remote authentication failed");
    }
    List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
    return new BggAuthenticationToken(cookies);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return Authentication.class.isAssignableFrom(authentication);
  }

}
