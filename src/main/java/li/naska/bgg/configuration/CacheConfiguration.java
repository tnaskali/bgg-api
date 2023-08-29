package li.naska.bgg.configuration;


import jakarta.validation.constraints.NotNull;
import li.naska.bgg.util.ReflectionUtils;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

import java.util.Arrays;

@Configuration
@ImportRuntimeHints(CacheConfiguration.CacheRuntimeHints.class)
public class CacheConfiguration {

  static class CacheRuntimeHints implements RuntimeHintsRegistrar {

    private static final String[] CACHE_REFLECTION_CLASSES = {
        "com.github.benmanes.caffeine.cache.PSWMW"
    };

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
      Arrays.stream(CACHE_REFLECTION_CLASSES)
          .map(ReflectionUtils::getClass)
          .forEach(clazz -> hints.reflection().registerType(
              clazz,
              MemberCategory.DECLARED_FIELDS,
              MemberCategory.INVOKE_DECLARED_METHODS,
              MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
          );
    }

  }

}
