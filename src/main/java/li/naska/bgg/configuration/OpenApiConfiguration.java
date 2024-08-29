package li.naska.bgg.configuration;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "BGG API", version = "v1"))
@SecuritySchemes({
  @SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
})
public class OpenApiConfiguration {

  static {
    PrimitiveType.enablePartialTime();
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
}
