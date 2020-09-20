package li.naska.bgg.security;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BggAuthenticationTokenTest {

  @Test
  public void authentication_WhenCookiesArePresent_ShouldBeSuccessful() {
    List<String> cookies = Arrays.asList(
        "SessionID=1234abcd;",
        "bggusername=foo;",
        "bggpassword=bar;"
    );

    BggAuthenticationToken authentication = new BggAuthenticationToken(cookies);

    assertThat(authentication.getPrincipal()).isEqualTo("foo");
    assertThat(authentication.getCredentials()).isEqualTo("bar");
    assertThat(authentication.getDetails()).isEqualTo("1234abcd");
  }

  @Test
  public void authentication_WhenUsernameCookieIsMissing_ShouldFail() {
    List<String> cookies = Arrays.asList(
        "SessionID=1234abcd;",
        "bggpassword=bar;"
    );

    assertThatThrownBy(() -> new BggAuthenticationToken(cookies)).hasMessage("no username cookie");
  }

  @Test
  public void authentication_WhenPasswordCookieIsMissing_ShouldFail() {
    List<String> cookies = Arrays.asList(
        "SessionID=1234abcd;",
        "bggusername=foo;"
    );

    assertThatThrownBy(() -> new BggAuthenticationToken(cookies)).hasMessage("no password cookie");
  }

  @Test
  public void authentication_WhenSessionIdCookieIsMissing_ShouldFail() {
    List<String> cookies = Arrays.asList(
        "bggusername=foo;",
        "bggpassword=bar;"
    );

    assertThatThrownBy(() -> new BggAuthenticationToken(cookies)).hasMessage("no sessionId cookie");
  }

}