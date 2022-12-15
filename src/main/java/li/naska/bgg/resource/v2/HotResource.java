package li.naska.bgg.resource.v2;

import com.boardgamegeek.hot.Items;
import li.naska.bgg.repository.BggHotV2Repository;
import li.naska.bgg.repository.model.BggHotV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("HotV2Resource")
@RequestMapping("/api/v2/hot")
public class HotResource {

  @Autowired
  private BggHotV2Repository hotItemsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getHotItemsAsXml(@Validated BggHotV2QueryParams params) {
    return hotItemsRepository.getHotItems(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getHotItemsAsJson(@Validated BggHotV2QueryParams params) {
    return getHotItemsAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Items.class));
  }

}
