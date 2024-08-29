package li.naska.bgg.configuration;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.stream.Stream;
import li.naska.bgg.util.ClasspathUtils;
import li.naska.bgg.util.ReflectionUtils;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(JaxbConfiguration.JaxbRuntimeHints.class)
public class JaxbConfiguration {

  static class JaxbRuntimeHints implements RuntimeHintsRegistrar {

    private static final String[] JAXB_MODEL_PACKAGES = {
      "com.boardgamegeek.boardgame.v1",
      "com.boardgamegeek.collection.v1",
      "com.boardgamegeek.company.v1",
      "com.boardgamegeek.geeklist.v1",
      "com.boardgamegeek.person.v1",
      "com.boardgamegeek.search.v1",
      "com.boardgamegeek.collection.v2",
      "com.boardgamegeek.company.v2",
      "com.boardgamegeek.family.v2",
      "com.boardgamegeek.forum.v2",
      "com.boardgamegeek.forumlist.v2",
      "com.boardgamegeek.guild.v2",
      "com.boardgamegeek.hot.v2",
      "com.boardgamegeek.person.v2",
      "com.boardgamegeek.plays.v2",
      "com.boardgamegeek.search.v2",
      "com.boardgamegeek.thing.v2",
      "com.boardgamegeek.thread.v2",
      "com.boardgamegeek.user.v2",
      "com.boardgamegeek.common"
    };

    private static final String[] JAXB_REFLECTION_CLASSES = {
      "jakarta.xml.bind.Binder",
      "jakarta.xml.bind.JAXBElement",
      "jakarta.xml.bind.annotation.XmlAccessType",
      "jakarta.xml.bind.annotation.XmlAccessorType",
      "jakarta.xml.bind.annotation.XmlAttribute",
      "jakarta.xml.bind.annotation.XmlElement",
      "jakarta.xml.bind.annotation.XmlElementDecl",
      "jakarta.xml.bind.annotation.XmlElementRef",
      "jakarta.xml.bind.annotation.XmlElementRefs",
      "jakarta.xml.bind.annotation.XmlEnum",
      "jakarta.xml.bind.annotation.XmlRootElement",
      "jakarta.xml.bind.annotation.XmlSchemaType",
      "jakarta.xml.bind.annotation.XmlType",
      "jakarta.xml.bind.annotation.XmlValue",
      "jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter",
      "java.util.ArrayList",
      "li.naska.bgg.util.DateTimeToZonedDateTimeAdapter",
      "li.naska.bgg.util.IntegerToIntegerAdapter",
      "li.naska.bgg.util.StringToLocalDateAdapter",
      "li.naska.bgg.util.StringToLocalDateTimeAdapter",
      "li.naska.bgg.util.StringToZonedDateTimeAdapter",
      "li.naska.bgg.util.XmlProcessor$JAXBElementMixin",
      "org.glassfish.jaxb.runtime.v2.runtime.property.ArrayElementLeafProperty",
      "org.glassfish.jaxb.runtime.v2.runtime.property.ArrayElementNodeProperty",
      "org.glassfish.jaxb.runtime.v2.runtime.property.ArrayReferenceNodeProperty",
      "org.glassfish.jaxb.runtime.v2.runtime.property.SingleElementLeafProperty",
      "org.glassfish.jaxb.runtime.v2.runtime.property.SingleElementNodeProperty",
      "org.glassfish.jaxb.runtime.v2.runtime.property.SingleMapNodeProperty",
      "org.glassfish.jaxb.runtime.v2.runtime.property.SingleReferenceNodeProperty"
    };

    private static final String[][] JAXB_PROXY_INTERFACES = {
      {
        "jakarta.xml.bind.annotation.XmlAccessorType",
        "org.glassfish.jaxb.core.v2.model.annotation.Locatable"
      },
      {
        "jakarta.xml.bind.annotation.XmlEnumValue",
        "org.glassfish.jaxb.core.v2.model.annotation.Locatable"
      },
      {
        "jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter",
        "org.glassfish.jaxb.core.v2.model.annotation.Locatable"
      }
    };

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
      Stream.concat(
              Arrays.stream(JAXB_MODEL_PACKAGES)
                  .flatMap(
                      packageName -> ClasspathUtils.getClassesInPackage(packageName, classLoader)),
              Arrays.stream(JAXB_REFLECTION_CLASSES).map(ReflectionUtils::getClass))
          .forEach(
              clazz ->
                  hints
                      .reflection()
                      .registerType(
                          clazz,
                          MemberCategory.DECLARED_FIELDS,
                          MemberCategory.INVOKE_DECLARED_METHODS,
                          MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));

      Arrays.stream(JAXB_PROXY_INTERFACES)
          .forEach(
              item ->
                  hints
                      .proxies()
                      .registerJdkProxy(
                          Arrays.stream(item)
                              .map(ReflectionUtils::getClass)
                              .toArray(Class<?>[]::new)));
    }
  }
}
