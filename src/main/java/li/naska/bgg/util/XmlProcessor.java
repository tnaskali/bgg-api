package li.naska.bgg.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.StaxUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Component
@Slf4j
public class XmlProcessor {

  private final ObjectMapper objectMapper;

  private final DocumentBuilderFactory documentBuilderFactory =
      DocumentBuilderFactory.newInstance();

  private final XPathFactory xPathFactory = XPathFactory.newInstance();

  public XmlProcessor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T toJavaObject(String xmlString, Class<T> targetClass) {
    try {
      JAXBContext context = JAXBContext.newInstance(targetClass);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      XMLInputFactory inputFactory = StaxUtils.createDefensiveInputFactory();
      XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlString));
      JAXBElement<T> jaxbElement = unmarshaller.unmarshal(eventReader, targetClass);
      return jaxbElement.getValue();
    } catch (JAXBException | XMLStreamException e) {
      log.error("XML processing error", e);
      throw new IllegalStateException(e);
    }
  }

  public <T> String toJsonString(String xmlString, Class<T> targetClass) {
    T object = toJavaObject(xmlString, targetClass);
    return toJsonString(object);
  }

  public <T> String toJsonString(T object) {
    try {
      return objectMapper
          .addMixIn(JAXBElement.class, JAXBElementMixin.class)
          .writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("JSON processing error", e);
      throw new IllegalStateException(e);
    }
  }

  @SneakyThrows
  public Optional<String> xPathValue(String text, String expression) {
    Document document = documentBuilderFactory
        .newDocumentBuilder()
        .parse(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
    Node node = (Node) xPathFactory.newXPath().evaluate(expression, document, XPathConstants.NODE);
    return Optional.ofNullable(node).map(Node::getTextContent);
  }

  abstract static class JAXBElementMixin<T> {
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
