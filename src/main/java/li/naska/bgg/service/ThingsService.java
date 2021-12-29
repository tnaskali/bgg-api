package li.naska.bgg.service;

import com.boardgamegeek.enums.CollectionItemSubtype;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.mapper.*;
import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.BggThingsRepository;
import li.naska.bgg.repository.model.BggCollectionQueryParams;
import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.resource.v3.model.*;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThingsService {

  @Autowired
  private BggCollectionRepository collectionRepository;

  @Autowired
  private CollectionParamsMapper collectionParamsMapper;

  @Autowired
  private CollectionMapper collectionMapper;

  @Autowired
  private BggThingsRepository thingsRepository;

  @Autowired
  private ThingsParamsMapper thingsParamsMapper;

  @Autowired
  private ThingParamsMapper thingParamsMapper;

  @Autowired
  private ThingMapper thingMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Thing> getThing(Integer id, ThingParams params) {
    BggThingsQueryParams bggParams = thingParamsMapper.toBggModel(params);
    bggParams.setId(id.toString());
    return thingsRepository.getThings(bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Things.class))
        .map(Things::getItem)
        .flatMap(l -> l.size() == 1
            ? Mono.just(l.get(0))
            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no match found")))
        .map(thingMapper::fromBggModel);
  }

  public Mono<List<Thing>> getThings(ThingsParams params) {
    BggThingsQueryParams bggParams = thingsParamsMapper.toBggModel(params);
    return thingsRepository.getThings(bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Things.class))
        .map(Things::getItem)
        .map(l -> l.stream()
            .map(thingMapper::fromBggModel)
            .collect(Collectors.toList()));

  }

  public Mono<Collection> getThings(String username, CollectionParams params) {
    BggCollectionQueryParams bggParams = collectionParamsMapper.toBggModel(params);
    bggParams.setUsername(username);
    // handle subtype bug in the BBG XML API
    if (bggParams.getSubtype() == null || bggParams.getSubtype().equals(CollectionItemSubtype.boardgame.value())) {
      bggParams.setExcludesubtype(CollectionItemSubtype.boardgameexpansion.value());
    }
    return collectionRepository.getCollection(null, bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.collection.Collection.class))
        .map(collectionMapper::fromBggModel);
  }

  public Mono<Collection> getPrivateThings(String username, String cookie, CollectionParams params) {
    BggCollectionQueryParams bggParams = collectionParamsMapper.toBggModel(params);
    bggParams.setUsername(username);
    // handle subtype bug in the BBG XML API
    if (bggParams.getSubtype() == null || bggParams.getSubtype().equals(CollectionItemSubtype.boardgame.value())) {
      bggParams.setExcludesubtype(CollectionItemSubtype.boardgameexpansion.value());
    }
    bggParams.setShowprivate(1);
    return collectionRepository.getCollection(cookie, bggParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.collection.Collection.class))
        .map(collectionMapper::fromBggModel);
  }

}
