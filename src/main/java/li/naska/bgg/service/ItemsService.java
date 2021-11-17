package li.naska.bgg.service;

import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.search.Results;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.BggHotRepository;
import li.naska.bgg.repository.BggSearchRepository;
import li.naska.bgg.repository.BggThingRepository;
import li.naska.bgg.repository.model.BggSearchParameters;
import li.naska.bgg.repository.model.BggThingsParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ItemsService {

  @Autowired
  private BggThingRepository thingRepository;

  @Autowired
  private BggHotRepository hotRepository;

  @Autowired
  private BggSearchRepository searchRepository;

  public Mono<Things> getThings(BggThingsParameters parameters) {
    return thingRepository.getThings(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Things.class));
  }

  public Mono<HotItems> getHotItems(HotItemType type) {
    return hotRepository.getItems(type)
        .map(xml -> new XmlProcessor(xml).toJavaObject(HotItems.class));
  }

  public Mono<Results> getResults(BggSearchParameters parameters) {
    return searchRepository.getResults(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Results.class));
  }

}
