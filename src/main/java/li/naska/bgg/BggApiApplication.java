package li.naska.bgg;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "BGG API", version = "v1"))
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class BggApiApplication {

  public static void main(String[] args) {
    PrimitiveType.customClasses().put("java.time.LocalTime", PrimitiveType.PARTIAL_TIME);
    SpringApplication.run(BggApiApplication.class, args);
  }

  @Bean
  public GroupedOpenApi bggApiV1() {
    return GroupedOpenApi.builder()
        .group("v1 - XML API (v1)")
        .pathsToMatch("/api/v1/**")
        .build();
  }

  @Bean
  public GroupedOpenApi bggApiV2() {
    return GroupedOpenApi.builder()
        .group("v2 - XML API (v2)")
        .pathsToMatch("/api/v2/**")
        .build();
  }

  @Bean
  public GroupedOpenApi bggApiV3() {
    return GroupedOpenApi.builder()
        .group("v3 - JSON API (PHP)")
        .pathsToMatch("/api/v3/**")
        .build();
  }

  @Bean
  public GroupedOpenApi bggApiV4() {
    return GroupedOpenApi.builder()
        .group("v4 - JSON API (Geekdo)")
        .pathsToMatch("/api/v4/**")
        .build();
  }

  @Bean
  public GroupedOpenApi bggApiV5() {
    return GroupedOpenApi.builder()
        .group("v5 - JSON API (Website)")
        .pathsToMatch("/api/v5/**")
        .build();
  }

  @Bean
  public GroupedOpenApi bggApiVN() {
    return GroupedOpenApi.builder()
        .group("vN - JSON API (NEW)")
        .pathsToMatch("/api/vN/**")
        .build();
  }

}
