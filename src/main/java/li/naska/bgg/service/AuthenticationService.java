package li.naska.bgg.service;

import java.util.Optional;
import li.naska.bgg.security.BggAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

  private Mono<SecurityContext> getSecurityContext() {
    return ReactiveSecurityContextHolder.getContext();
  }

  private Mono<BggAuthenticationToken> authentication() {
    return getSecurityContext()
        .map(context -> (BggAuthenticationToken) context.getAuthentication());
  }

  public Mono<BggAuthenticationToken> requiredAuthentication() {
    return authentication()
        .switchIfEmpty(Mono.error(
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authentication required")));
  }

  public Mono<Optional<BggAuthenticationToken>> optionalAuthentication() {
    return authentication().singleOptional();
  }
}
