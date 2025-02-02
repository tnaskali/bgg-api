package li.naska.bgg.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@UtilityClass
public class ClasspathUtils {

  public static Stream<Class<?>> getClassesInPackage(
      @NotNull String packageName, @NotNull ClassLoader classLoader) {
    InputStream stream = classLoader.getResourceAsStream(packageName.replaceAll("[.]", "/"));
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    return reader
        .lines()
        .filter(line -> line.endsWith(".class"))
        .map(line -> packageName + "." + line.substring(0, line.lastIndexOf('.')))
        .map(ReflectionUtils::getClass)
        .flatMap(ReflectionUtils::getDeclaredClasses);
  }
}
