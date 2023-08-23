package li.naska.bgg.cache;

import com.github.benmanes.caffeine.cache.AsyncCache;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author shaikezr
 * @see <a href="https://github.com/shaikezr/async-cacheable">async-cacheable</a>
 */
@RequiredArgsConstructor
@Aspect
@Component
public class AsyncCacheManager {

  private final Map<String, AsyncCache<String, ?>> caches = new ConcurrentHashMap<>();

  public <T> AsyncCache<String, T> get(String name) {
    return (AsyncCache<String, T>) this.caches.get(name);
  }

  public <T> void createIfAbsent(String name, Function<? super String, ? extends AsyncCache<String, T>> mappingFunction) {
    this.caches.computeIfAbsent(name, mappingFunction);
  }

}