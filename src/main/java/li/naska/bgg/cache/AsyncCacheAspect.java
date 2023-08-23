package li.naska.bgg.cache;

import com.github.benmanes.caffeine.cache.AsyncCache;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author shaikezr
 * @see <a href="https://github.com/shaikezr/async-cacheable">async-cacheable</a>
 */
@RequiredArgsConstructor
@Aspect
@Component
public class AsyncCacheAspect {

  private final AsyncCacheManager asyncCacheManager;

  @Pointcut("@annotation(AsyncCacheable)")
  public void pointcut() {
  }

  @Around("pointcut()")
  public Object around(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();

    ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
    Type rawType = parameterizedType.getRawType();

    if (!rawType.equals(Mono.class)) {
      throw new IllegalArgumentException(
          "Method return type must be Mono<?> (method: %s)".formatted(method.getName()));
    }

    String cacheIdentifier = getCacheIdentifier(method);
    AsyncCache<String, ?> asyncCache = asyncCacheManager.get(cacheIdentifier);
    if (Objects.isNull(asyncCache)) {
      return joinPoint.proceed();
    }

    Mono<?> retVal =
        Mono.defer(
            () -> {
              try {
                return (Mono<?>) joinPoint.proceed();
              } catch (Throwable cause) {
                return Mono.error(new CacheException("Error processing async cache", cause));
              }
            });
    String cacheKey = getCacheKey(method, joinPoint.getArgs());
    CompletableFuture<?> completableFuture =
        asyncCache.get(cacheKey, (key, exec) -> (CompletableFuture) retVal.toFuture());
    return Mono.fromFuture(completableFuture);
  }

  private static String getCacheIdentifier(@NotNull Method method) {
    AsyncCacheable asyncCacheable = method.getAnnotation(AsyncCacheable.class);
    return "%s_%s_%s".formatted(
        asyncCacheable.name(),
        asyncCacheable.expireAfterWriteSeconds(),
        asyncCacheable.maximumSize());
  }

  private static @NotNull String getCacheKey(Method method, Object... methodArguments) {
    return "%s#".formatted(method.hashCode())
        + Arrays.stream(methodArguments)
        .map(parameter -> parameter == null ? "" : parameter.toString())
        .collect(Collectors.joining("#"));
  }

}
