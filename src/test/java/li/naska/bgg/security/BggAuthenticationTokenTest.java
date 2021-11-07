package li.naska.bgg.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BggAuthenticationTokenTest {

  @Test
  public void authentication_WhenCookiesArePresent_ShouldBeSuccessful() {
    String sessionIdCookie = "SessionID=1234abcd; expires=Mon, 01-Nov-2021 11:40:40 GMT; Max-Age=3600; path=/; domain=.boardgamegeek.com";
    String usernameCookie = "bggusername=foo; expires=Wed, 01-Dec-2021 13:42:33 GMT; Max-Age=2592000; path=/; domain=.boardgamegeek.com";
    String passwordCookie = "bggpassword=bar_encoded; expires=Wed, 01-Dec-2021 13:42:33 GMT; Max-Age=2592000; path=/; domain=.boardgamegeek.com";

    BggAuthenticationToken authentication = new BggAuthenticationToken(sessionIdCookie, usernameCookie, passwordCookie);

    assertThat(authentication.getPrincipal()).isEqualTo("foo");
    assertThat(authentication.getCredentials()).isEqualTo("bar_encoded");
    assertThat(authentication.getDetails()).isEqualTo("1234abcd");
  }

  @Test
  public void authentication_WhenUsernameCookieIsMissing_ShouldFail() {
    String sessionIdCookie = "SessionID=1234abcd; expires=Mon, 01-Nov-2021 11:40:40 GMT; Max-Age=3600; path=/; domain=.boardgamegeek.com";
    String usernameCookie = null;
    String passwordCookie = "bggpassword=bar_encoded; expires=Wed, 01-Dec-2021 13:42:33 GMT; Max-Age=2592000; path=/; domain=.boardgamegeek.com";

    assertThatThrownBy(() -> new BggAuthenticationToken(sessionIdCookie, usernameCookie, passwordCookie)).hasMessage("username cookie is null");
  }

  @Test
  public void authentication_WhenPasswordCookieIsMissing_ShouldFail() {
    String sessionIdCookie = "SessionID=1234abcd; expires=Mon, 01-Nov-2021 11:40:40 GMT; Max-Age=3600; path=/; domain=.boardgamegeek.com";
    String usernameCookie = "bggusername=foo; expires=Wed, 01-Dec-2021 13:42:33 GMT; Max-Age=2592000; path=/; domain=.boardgamegeek.com";
    String passwordCookie = null;

    assertThatThrownBy(() -> new BggAuthenticationToken(sessionIdCookie, usernameCookie, passwordCookie)).hasMessage("password cookie is null");
  }

  @Test
  public void authentication_WhenSessionIdCookieIsMissing_ShouldFail() {
    String sessionIdCookie = null;
    String usernameCookie = "bggusername=foo; expires=Wed, 01-Dec-2021 13:42:33 GMT; Max-Age=2592000; path=/; domain=.boardgamegeek.com";
    String passwordCookie = "bggpassword=bar_encoded; expires=Wed, 01-Dec-2021 13:42:33 GMT; Max-Age=2592000; path=/; domain=.boardgamegeek.com";


    assertThatThrownBy(() -> new BggAuthenticationToken(sessionIdCookie, usernameCookie, passwordCookie)).hasMessage("sessionId cookie is null");
  }

}