package li.naska.bgg.util;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HtmlProcessor {

  public Document parse(String html) {
    return Jsoup.parse(html);
  }

  public Optional<String> getFirstElementTextByClass(String html, String elementClass) {
    return getFirstElementTextByClass(parse(html), elementClass);
  }

  public Optional<String> getFirstElementTextByClass(Document document, String elementClass) {
    return Optional.ofNullable(document.getElementsByClass(elementClass).first())
        .flatMap(e ->
            Optional.ofNullable(e.firstChild()).map(node -> node.attr("#text")).map(String::trim));
  }

  public Optional<String> getElementValueById(String html, String elementClass) {
    return getElementValueById(parse(html), elementClass);
  }

  public Optional<String> getElementValueById(Document document, String elementId) {
    return Optional.ofNullable(document.getElementById(elementId)).map(e -> e.attr("value"));
  }

  public Optional<String> getElementValueByName(String html, String elementClass) {
    return getElementValueByName(parse(html), elementClass);
  }

  public Optional<String> getElementValueByName(Document document, String elementName) {
    return Optional.ofNullable(
            document.getElementsByAttributeValue("name", elementName).first())
        .map(e -> e.attr("value"));
  }

  public Optional<String> getSelectedElementValueByName(String html, String elementClass) {
    return getSelectedElementValueByName(parse(html), elementClass);
  }

  public Optional<String> getSelectedElementValueByName(Document document, String elementId) {
    return Optional.ofNullable(document.getElementById(elementId))
        .map(Element::children)
        .flatMap(
            elements -> elements.stream().filter(e -> e.hasAttr("SELECTED")).findFirst())
        .map(e -> e.attr("value"));
  }
}
