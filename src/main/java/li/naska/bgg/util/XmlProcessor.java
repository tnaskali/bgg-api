package li.naska.bgg.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.StaxUtils;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.StringReader;

@Component
public class XmlProcessor {

  @Autowired
  private ObjectMapper objectMapper;

  @SneakyThrows
  public <T> T toJavaObject(String xmlString, Class<T> targetClass) {
    JAXBContext context = JAXBContext.newInstance(targetClass);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    XMLInputFactory inputFactory = StaxUtils.createDefensiveInputFactory();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlString));
    JAXBElement<T> jaxbElement = unmarshaller.unmarshal(eventReader, targetClass);
    return jaxbElement.getValue();
  }

  @SneakyThrows
  public <T> String toJsonString(String xmlString, Class<T> targetClass) {
    T object = toJavaObject(xmlString, targetClass);
    return objectMapper
        .addMixIn(JAXBElement.class, JAXBElementMixin.class)
        .writeValueAsString(object);
  }

  static abstract class JAXBElementMixin<T> {
    @JsonIgnore
    protected abstract Class<T> getDeclaredType();

    @JsonIgnore
    protected abstract Class<?> getScope();

    @JsonIgnore
    protected abstract boolean isNil();

    @JsonIgnore
    protected abstract boolean isGlobalScope();

    @JsonIgnore
    protected abstract boolean isTypeSubstituted();
  }

}
