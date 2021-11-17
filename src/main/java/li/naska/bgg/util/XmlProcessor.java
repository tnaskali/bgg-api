package li.naska.bgg.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import org.springframework.util.xml.StaxUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import java.io.StringReader;

public class XmlProcessor {

  private final String xmlString;

  public XmlProcessor(String xmlString) {
    this.xmlString = xmlString;
  }

  @SneakyThrows
  public <T> T toJavaObject(Class<T> targetClass) {
    JAXBContext context = JAXBContext.newInstance(targetClass);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    XMLInputFactory inputFactory = StaxUtils.createDefensiveInputFactory();
    XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlString));
    JAXBElement<T> jaxbElement = unmarshaller.unmarshal(eventReader, targetClass);
    return jaxbElement.getValue();
  }

  @SneakyThrows
  public String toJsonString() {
    XmlMapper xmlMapper = new XmlMapper();
    JsonNode node = xmlMapper.readTree(xmlString.getBytes());
    ObjectMapper jsonMapper = new ObjectMapper();
    return jsonMapper.writeValueAsString(node);
  }

}
