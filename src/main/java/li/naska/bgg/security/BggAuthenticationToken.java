package li.naska.bgg.security;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class BggAuthenticationToken extends AbstractAuthenticationToken {

  private static final Pattern SESSIONID_COOKIE_PATTERN = Pattern.compile("^SessionID=([^;]+);");

  private static final Pattern BGGUSERNAME_COOKIE_PATTERN =
      Pattern.compile("^bggusername=([^;]+);");

  private static final Pattern BGGPASSWORD_COOKIE_PATTERN =
      Pattern.compile("^bggpassword=([^;]+);");

  private final String bggUsername;

  private final String bggPassword;

  private final String username;

  private final String password;

  public BggAuthenticationToken(String username, String password, List<String> setCookieHeaders) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.username = username;
    this.password = password;
    String bggSessionId = setCookieHeaders.stream()
        .filter(e -> e.startsWith("SessionID"))
        .findFirst()
        .flatMap(sessionIdCookie -> {
          Matcher sessionIdMatcher = SESSIONID_COOKIE_PATTERN.matcher(sessionIdCookie);
          return sessionIdMatcher.find()
              ? Optional.of(sessionIdMatcher.group(1))
              : Optional.empty();
        })
        .orElseThrow(() -> new IllegalStateException("no sessionId cookie found"));
    setDetails(bggSessionId);
    bggUsername = setCookieHeaders.stream()
        .filter(e -> !e.startsWith("bggusername=deleted;"))
        .filter(e -> e.startsWith("bggusername"))
        .findFirst()
        .flatMap(usernameCookie -> {
          Matcher usernameMatcher = BGGUSERNAME_COOKIE_PATTERN.matcher(usernameCookie);
          return usernameMatcher.find() ? Optional.of(usernameMatcher.group(1)) : Optional.empty();
        })
        .orElseThrow(() -> new IllegalStateException("no username cookie found"));
    bggPassword = setCookieHeaders.stream()
        .filter(e -> !e.startsWith("bggpassword=deleted;"))
        .filter(e -> e.startsWith("bggpassword"))
        .findFirst()
        .flatMap(passwordCookie -> {
          Matcher passwordMatcher = BGGPASSWORD_COOKIE_PATTERN.matcher(passwordCookie);
          return passwordMatcher.find() ? Optional.of(passwordMatcher.group(1)) : Optional.empty();
        })
        .orElseThrow(() -> new IllegalStateException("no password cookie found"));
    setAuthenticated(true);
  }

  @Override
  public String getPrincipal() {
    return username;
  }

  @Override
  public String getCredentials() {
    return password;
  }

  public String getBggUsername() {
    return bggUsername;
  }

  public String getBggPassword() {
    return bggPassword;
  }

  @Override
  public String getDetails() {
    return (String) super.getDetails();
  }

  public String buildBggRequestHeader() {
    String sessionId = getDetails();
    return String.format(
        "SessionID=%s; bggusername=%s; bggpassword=%s", sessionId, bggUsername, bggPassword);
  }
}
