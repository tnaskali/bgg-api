package li.naska.bgg.security;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BggAuthenticationProvider implements AuthenticationProvider {

  private static final Pattern SESSION_COOKIE_PATTERN = Pattern.compile("^SessionID=([^;]+);");

  private static final Pattern USERNAME_COOKIE_PATTERN = Pattern.compile("^bggusername=([^;]+);");

  private static final Pattern PASSWORD_COOKIE_PATTERN = Pattern.compile("^bggpassword=([^;]+);");

  @Value("${bgg.endpoints.login.write}")
  private String loginEndpoint;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {

    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("username", username);
    formData.add("password", password);
    ResponseEntity<String> response = restTemplate.exchange(loginEndpoint, HttpMethod.POST, new HttpEntity<>(formData, requestHeaders), String.class);
    if (response.getStatusCode() != HttpStatus.OK) {
      throw new AuthenticationServiceException("Remote authentication failed");
    }
    List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
    if (cookies.size() < 3) {
      throw new AuthenticationServiceException("Remote authentication failed");
    }

    String sessionCookie = cookies.stream()
        .filter(e -> SESSION_COOKIE_PATTERN.matcher(e).find())
        .map(e -> {
          Matcher matcher = SESSION_COOKIE_PATTERN.matcher(e);
          matcher.find();
          return matcher.group(1);
        })
        .findFirst()
        .orElseThrow(() -> new AuthenticationServiceException("Remote authentication failed"));
    String usernameCookie = cookies.stream()
        .filter(e -> USERNAME_COOKIE_PATTERN.matcher(e).find())
        .map(e -> {
          Matcher matcher = USERNAME_COOKIE_PATTERN.matcher(e);
          matcher.find();
          return matcher.group(1);
        })
        .findFirst()
        .orElseThrow(() -> new AuthenticationServiceException("Remote authentication failed"));
    String passwordCookie = cookies.stream()
        .filter(e -> PASSWORD_COOKIE_PATTERN.matcher(e).find())
        .map(e -> {
          Matcher matcher = PASSWORD_COOKIE_PATTERN.matcher(e);
          matcher.find();
          return matcher.group(1);
        })
        .findFirst()
        .orElseThrow(() -> new AuthenticationServiceException("Remote authentication failed"));

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        usernameCookie, passwordCookie, AuthorityUtils.NO_AUTHORITIES
    );
    token.setDetails(sessionCookie);
    return token;
  }

  HttpHeaders createHeaders(String username, String password) {
    String auth = username + ":" + password;
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
    String authHeader = "Basic " + new String(encodedAuth);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", authHeader);
    headers.set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
    return headers;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

}
