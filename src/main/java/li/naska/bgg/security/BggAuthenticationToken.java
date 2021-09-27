package li.naska.bgg.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BggAuthenticationToken extends AbstractAuthenticationToken {

  private static final Pattern SESSIONID_COOKIE_PATTERN = Pattern.compile("^SessionID=([^;]+);");

  private static final Pattern BGGUSERNAME_COOKIE_PATTERN = Pattern.compile("^bggusername=([^;]+);");

  private static final Pattern BGGPASSWORD_COOKIE_PATTERN = Pattern.compile("^bggpassword=([^;]+);");

  private final String bggUsername;

  private final String bggPassword;

  public BggAuthenticationToken(List<String> cookies) {
    super(AuthorityUtils.NO_AUTHORITIES);
    if (cookies == null) {
      throw new AuthenticationServiceException("no cookies");
    }
    String bggSessionId = cookies.stream()
        .map(SESSIONID_COOKIE_PATTERN::matcher)
        .flatMap(e -> e.find() ? Stream.of(e.group(1)) : Stream.empty())
        .findFirst()
        .orElseThrow(() -> new AuthenticationServiceException("no SessionId cookie"));
    setDetails(bggSessionId);
    bggUsername = cookies.stream()
        .map(BGGUSERNAME_COOKIE_PATTERN::matcher)
        .flatMap(e -> e.find() ? Stream.of(e.group(1)) : Stream.empty())
        .findFirst()
        .orElseThrow(() -> new AuthenticationServiceException("no username cookie"));
    bggPassword = cookies.stream()
        .map(BGGPASSWORD_COOKIE_PATTERN::matcher)
        .flatMap(e -> e.find() ? Stream.of(e.group(1)) : Stream.empty())
        .findFirst()
        .orElseThrow(() -> new AuthenticationServiceException("no password cookie"));
    setAuthenticated(true);
  }

  @Override
  public String getPrincipal() {
    return bggUsername;
  }

  @Override
  public String getCredentials() {
    return bggPassword;
  }

  @Override
  public String getDetails() {
    return (String) super.getDetails();
  }

  public String buildBggRequestHeader() {
    String sessionId = getDetails();
    return String.format("SessionID=%s; bggusername=%s; bggpassword=%s", sessionId, bggUsername, bggPassword);
  }

}
