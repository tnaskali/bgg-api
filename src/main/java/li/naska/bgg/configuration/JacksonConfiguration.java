package li.naska.bgg.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;
import li.naska.bgg.util.*;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.module.SimpleDeserializers;
import tools.jackson.databind.module.SimpleModule;

@Configuration
@ImportRuntimeHints(JacksonConfiguration.JacksonRuntimeHints.class)
public class JacksonConfiguration {

  @Bean
  public JsonMapperBuilderCustomizer objectMapperBuilderCustomizer() {
    return builder -> builder
        // Use ISO-8601 for dates
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        // see BggUserV4ResponseBody#adminBadges
        .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
        // set to enabled to increase strictness
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(Include.NON_NULL))
        .addModule(new SafeJavaTimeModule())
        .addMixIn(JAXBElement.class, JAXBElementMixin.class);
  }

  interface JAXBElementMixin<T> {
    @JsonIgnore
    Class<T> getDeclaredType();

    @JsonIgnore
    Class<?> getScope();

    @JsonIgnore
    boolean isNil();

    @JsonIgnore
    boolean isGlobalScope();

    @JsonIgnore
    boolean isTypeSubstituted();
  }

  public static class SafeJavaTimeModule extends SimpleModule {
    @Override
    public void setupModule(SetupContext context) {
      SimpleDeserializers deserializers = new SimpleDeserializers();
      // see BggGeekitemV4ResponseBody#commercelinks
      deserializers.addDeserializer(
          LocalDateTime.class, SafeLocalDateTimeJacksonDeserializer.INSTANCE);
      // see BggGeekitemV4ResponseBody#commercelinks
      deserializers.addDeserializer(LocalDate.class, SafeLocalDateJacksonDeserializer.INSTANCE);
      context.addDeserializers(deserializers);
    }
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
                  MemberCategory.ACCESS_DECLARED_FIELDS,
                  MemberCategory.INVOKE_DECLARED_METHODS,
                  MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
    }
  }
}
