package li.naska.bgg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class BggApiSecurityConfiguration {

  @Autowired
  private BggAuthenticationManager authenticationManager;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .authenticationManager(authenticationManager)
        .csrf()
        .disable()
        .authorizeExchange()
        .pathMatchers("/api/v3/private/**")
        .authenticated()
        .anyExchange()
        .permitAll()
        .and()
        .httpBasic()
        .and()
        .build();
  }

}