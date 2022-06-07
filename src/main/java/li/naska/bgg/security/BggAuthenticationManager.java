package li.naska.bgg.security;

import li.naska.bgg.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BggAuthenticationManager implements ReactiveAuthenticationManager {

  @Autowired
  private LoginService loginService;

  @Override
  public Mono<Authentication> authenticate(final Authentication authentication) throws AuthenticationException {
    final String username = authentication.getName();
    final String password = authentication.getCredentials().toString();

    return loginService
        .login(username, password)
        .map(BggAuthenticationToken::new);
  }

}
