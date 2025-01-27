package li.naska.bgg.configuration;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient.Builder webClientBuilder() {
    // override Netty HttpClient default behaviour (no max idle time) because connections get closed
    // by BGG after some time
    ConnectionProvider connectionProvider = ConnectionProvider.builder("customConnectionPool")
        .maxIdleTime(Duration.ofMillis(60000))
        .maxLifeTime(Duration.ofMinutes(5))
        .build();

    HttpClient httpClient = HttpClient.create(connectionProvider).followRedirect(true);

    return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
  }
}
