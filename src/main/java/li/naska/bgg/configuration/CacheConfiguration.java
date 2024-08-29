package li.naska.bgg.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import li.naska.bgg.util.ReflectionUtils;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@EnableCaching
@ImportRuntimeHints(CacheConfiguration.CacheRuntimeHints.class)
public class CacheConfiguration {

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder().maximumSize(100).expireAfterWrite(60, TimeUnit.SECONDS);
  }

  @Bean
  public CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);
    caffeineCacheManager.setAsyncCacheMode(true);
    caffeineCacheManager.setAllowNullValues(false);
    return caffeineCacheManager;
  }

  static class CacheRuntimeHints implements RuntimeHintsRegistrar {

    private static final String[] CACHE_REFLECTION_CLASSES = {
      "com.github.benmanes.caffeine.cache.PSWMW"
    };

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
      Arrays.stream(CACHE_REFLECTION_CLASSES).map(ReflectionUtils::getClass).forEach(clazz -> hints
          .reflection()
          .registerType(
              clazz,
              MemberCategory.DECLARED_FIELDS,
              MemberCategory.INVOKE_DECLARED_METHODS,
              MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
    }
  }
}
