package li.naska.bgg.resource.v0;

import com.boardgamegeek.enums.HotItemType;
import li.naska.bgg.repository.BggHotRepository;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/hot")
public class HotResource {

  @Autowired
  private BggHotRepository hotItemsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getHotItemsAsXml(HotItemType type) {
    return hotItemsRepository.getItems(type);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getHotItemsAsJson(HotItemType type) {
    return getHotItemsAsXml(type)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
