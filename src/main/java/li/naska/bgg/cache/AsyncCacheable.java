package li.naska.bgg.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation used for async caching
 *
 * @author shaikezr
 * @see <a href="https://github.com/shaikezr/async-cacheable">async-cacheable</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AsyncCacheable {

  String name() default "default";

  long maximumSize() default 100L;

  long expireAfterWriteSeconds() default 60L;

}
