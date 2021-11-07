package li.naska.bgg.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BggAuthenticationToken extends AbstractAuthenticationToken {

  private static final Pattern SESSIONID_COOKIE_PATTERN = Pattern.compile("^SessionID=([^;]+);");

  private static final Pattern BGGUSERNAME_COOKIE_PATTERN = Pattern.compile("^bggusername=([^;]+);");

  private static final Pattern BGGPASSWORD_COOKIE_PATTERN = Pattern.compile("^bggpassword=([^;]+);");

  private final String bggUsername;

  private final String bggPassword;

  public BggAuthenticationToken(String sessionCookie, String usernameCookie, String passwordCookie) {
    super(AuthorityUtils.NO_AUTHORITIES);
    Assert.notNull(sessionCookie, "sessionId cookie is null");
    Assert.notNull(usernameCookie, "username cookie is null");
    Assert.notNull(passwordCookie, "password cookie is null");
    Matcher matcher = SESSIONID_COOKIE_PATTERN.matcher(sessionCookie);
    if (!matcher.find()) {
      throw new AuthenticationServiceException("SessionId not found in cookie");
    }
    String bggSessionId = matcher.group(1);
    setDetails(bggSessionId);
    Matcher usernameMatcher = BGGUSERNAME_COOKIE_PATTERN.matcher(usernameCookie);
    if (!usernameMatcher.find()) {
      throw new AuthenticationServiceException("bggusername not found in cookie");
    }
    this.bggUsername = usernameMatcher.group(1);
    Matcher passwordMatcher = BGGPASSWORD_COOKIE_PATTERN.matcher(passwordCookie);
    if (!passwordMatcher.find()) {
      throw new AuthenticationServiceException("bggpassword not found in cookie");
    }
    this.bggPassword = passwordMatcher.group(1);
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
