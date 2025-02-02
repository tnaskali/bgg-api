package li.naska.bgg.configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;
import li.naska.bgg.util.ClasspathUtils;
import li.naska.bgg.util.ReflectionUtils;
import li.naska.bgg.util.SafeLocalDateJacksonDeserializer;
import li.naska.bgg.util.SafeLocalDateTimeJacksonDeserializer;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(JacksonConfiguration.JacksonRuntimeHints.class)
public class JacksonConfiguration {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
    return builder -> {
      // Use ISO-8601 for dates
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      // see BggUserV4ResponseBody#adminBadges
      builder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
      // set to true to increase strictness
      builder.failOnUnknownProperties(false);
      builder.postConfigurer(objectMapper -> {
        // FIXME https://stackoverflow.com/a/56008395/4074057
        objectMapper.setDefaultPropertyInclusion(Include.NON_NULL);
        SimpleModule javaTimeModule = new JavaTimeModule();
        // deserialize bad formatted dates to null
        javaTimeModule.addDeserializer(
            LocalDateTime.class, SafeLocalDateTimeJacksonDeserializer.INSTANCE);
        // see BggGeekitemV4ResponseBody#commercelinks
        javaTimeModule.addDeserializer(LocalDate.class, SafeLocalDateJacksonDeserializer.INSTANCE);
        objectMapper.registerModule(javaTimeModule);
      });
    };
  }

  static class JacksonRuntimeHints implements RuntimeHintsRegistrar {

    private static final String JACKSON_MODEL_PACKAGE = "li.naska.bgg.repository.model";

    private static final String[] JACKSON_CLASSES = {
      "com.fasterxml.jackson.databind.ext.CoreXMLSerializers"
    };

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
      Stream.concat(
              ClasspathUtils.getClassesInPackage(JACKSON_MODEL_PACKAGE, classLoader),
              Arrays.stream(JACKSON_CLASSES).map(ReflectionUtils::getClass))
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
