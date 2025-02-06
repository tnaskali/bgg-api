package li.naska.bgg.configuration;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import li.naska.bgg.util.ReflectionUtils;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@OpenAPIDefinition(info = @Info(title = "BGG API", version = "v1"))
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@ImportRuntimeHints(OpenApiConfiguration.OpenApiRuntimeHints.class)
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

  static class OpenApiRuntimeHints implements RuntimeHintsRegistrar {

    private static final String[] OPENAPI_REFLECTION_CLASSES = {
      "io.swagger.v3.core.jackson.mixin.Schema31Mixin$TypeSerializer"
    };

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
      Arrays.stream(OPENAPI_REFLECTION_CLASSES)
          .map(ReflectionUtils::getClass)
          .forEach(clazz -> hints
              .reflection()
              .registerType(
                  clazz,
                  MemberCategory.DECLARED_FIELDS,
                  MemberCategory.INVOKE_DECLARED_METHODS,
                  MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
    }
  }
}
