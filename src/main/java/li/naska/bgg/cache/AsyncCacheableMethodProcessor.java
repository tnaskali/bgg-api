package li.naska.bgg.cache;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author shaikezr
 * @see <a href="https://github.com/shaikezr/async-cacheable">async-cacheable</a>
 */
@RequiredArgsConstructor
@Component
public class AsyncCacheableMethodProcessor implements BeanPostProcessor {

  private final AsyncCacheManager asyncCacheManager;

  @Override
  public Object postProcessBeforeInitialization(Object bean, @NotNull String beanName)
      throws BeansException {
    Arrays.stream(bean.getClass().getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(AsyncCacheable.class))
        .forEach(
            m -> {
              AsyncCacheable asyncCacheable = m.getAnnotation(AsyncCacheable.class);
              String cacheName = asyncCacheable.name();
              long expireAfterWriteSeconds = asyncCacheable.expireAfterWriteSeconds();
              long maximumSize = asyncCacheable.maximumSize();
              String cacheIdentifier =
                  "%s_%s_%s".formatted(cacheName, expireAfterWriteSeconds, maximumSize);
              asyncCacheManager.createIfAbsent(
                  cacheIdentifier,
                  (key) -> Caffeine.newBuilder()
                      .maximumSize(maximumSize)
                      .expireAfterWrite(expireAfterWriteSeconds, TimeUnit.SECONDS)
                      .buildAsync());
            });
    return bean;
  }

}
