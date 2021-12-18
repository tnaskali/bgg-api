package li.naska.bgg.service;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.search.Results;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.mapper.CollectionParamsMapper;
import li.naska.bgg.mapper.ResultsParamsMapper;
import li.naska.bgg.mapper.ThingsParamsMapper;
import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.BggHotItemsRepository;
import li.naska.bgg.repository.BggSearchRepository;
import li.naska.bgg.repository.BggThingsRepository;
import li.naska.bgg.repository.model.BggCollectionQueryParams;
import li.naska.bgg.repository.model.BggHotItemsQueryParams;
import li.naska.bgg.repository.model.BggSearchQueryParams;
import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.resource.v3.model.CollectionParams;
import li.naska.bgg.resource.v3.model.ResultsParams;
import li.naska.bgg.resource.v3.model.ThingsParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ItemsService {

  @Autowired
  private BggCollectionRepository collectionRepository;

  @Autowired
  private CollectionParamsMapper collectionParamsMapper;

  @Autowired
  private BggThingsRepository thingsRepository;

  @Autowired
  private ThingsParamsMapper thingsParamsMapper;

  @Autowired
  private BggHotItemsRepository hotItemsRepository;

  @Autowired
  private BggSearchRepository searchRepository;

  @Autowired
  private ResultsParamsMapper resultsParamsMapper;

  public Mono<Things> getThings(ThingsParams params) {
    BggThingsQueryParams bggParams = thingsParamsMapper.toBggModel(params);
    return thingsRepository.getThings(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Things.class));
  }

  public Mono<HotItems> getHotItems(HotItemType type) {
    BggHotItemsQueryParams bggParams = new BggHotItemsQueryParams();
    bggParams.setType(type.value());
    return hotItemsRepository.getHotItems(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(HotItems.class));
  }

  public Mono<Results> getResults(ResultsParams params) {
    BggSearchQueryParams bggParams = resultsParamsMapper.toBggModel(params);
    return searchRepository.getResults(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Results.class));
  }

  public Mono<Collection> getCollection(CollectionParams params) {
    BggCollectionQueryParams bggParams = collectionParamsMapper.toBggModel(params);
    // handle subtype bug in the BBG XML API
    if (bggParams.getSubtype() == null || bggParams.getSubtype().equals("boardgame")) {
      bggParams.setExcludesubtype("boardgameexpansion");
    }
    return collectionRepository.getCollection(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Collection.class));
  }

}
