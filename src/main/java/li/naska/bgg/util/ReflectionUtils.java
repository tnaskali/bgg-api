package li.naska.bgg.util;

import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ReflectionUtils {

  @SneakyThrows
  public static @NotNull Class<?> getClass(@NotNull String className) {
    return Class.forName(className);
  }

  public static Stream<? extends Class<?>> getDeclaredClasses(Class<?> clazz) {
    List<Class<?>> result = new ArrayList<>();
    result.add(clazz);
    result.addAll(List.of(clazz.getDeclaredClasses()));
    return result.stream();
  }

}
