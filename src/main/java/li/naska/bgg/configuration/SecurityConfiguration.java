package li.naska.bgg.configuration;

import li.naska.bgg.security.BggAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfiguration {

  @Autowired
  private BggAuthenticationManager authenticationManager;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .authenticationManager(authenticationManager)
        .csrf(CsrfSpec::disable)
        .authorizeExchange(spec -> spec
            .pathMatchers("/api/*/*/current/**")
            .authenticated()
            .anyExchange()
            .permitAll())
        .httpBasic(spec -> {
        })
        .build();
  }

}
