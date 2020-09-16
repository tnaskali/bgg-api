package li.naska.bgg;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BggApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BggApiApplication.class, args);
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("bgg-api")
        .pathsToExclude("/error")
        .build();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
