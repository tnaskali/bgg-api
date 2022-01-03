package li.naska.bgg.service;

import com.boardgamegeek.enums.CollectionItemSubtype;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.mapper.CollectionMapper;
import li.naska.bgg.mapper.CollectionParamsMapper;
import li.naska.bgg.mapper.ThingMapper;
import li.naska.bgg.mapper.ThingsParamsMapper;
import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.BggThingsRepository;
import li.naska.bgg.repository.model.BggCollectionQueryParams;
import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.resource.v3.model.Collection;
import li.naska.bgg.resource.v3.model.CollectionParams;
import li.naska.bgg.resource.v3.model.Thing;
import li.naska.bgg.resource.v3.model.Thing.Comment;
import li.naska.bgg.resource.v3.model.Thing.MarketplaceListing;
import li.naska.bgg.resource.v3.model.Thing.Version;
import li.naska.bgg.resource.v3.model.Thing.Video;
import li.naska.bgg.resource.v3.model.ThingsParams;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingHelper;
import li.naska.bgg.util.PagingParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThingsService {

  private static final int BGG_THING_COMMENTS_PAGE_SIZE = 100;

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
  private ThingMapper thingMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Thing> getThing(Integer id) {
    BggThingsQueryParams queryParams = new BggThingsQueryParams();
    queryParams.setId(id.toString());
    queryParams.setStats(1);
    return getThing(queryParams);
  }

  public Mono<List<Comment>> getComments(Integer id) {
    BggThingsQueryParams firstPageQueryParams = new BggThingsQueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setComments(1);
    firstPageQueryParams.setPage(1);
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> {
          int numPages = (int) Math.ceil((double) thing.getNumcomments() / BGG_THING_COMMENTS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                BggThingsQueryParams queryParams = new BggThingsQueryParams();
                queryParams.setId(id.toString());
                queryParams.setComments(1);
                queryParams.setPage(page);
                queryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
                return getThing(queryParams);
              })
              .flatMapIterable(Thing::getComments)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Comment>> getPagedComments(Integer id, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_THING_COMMENTS_PAGE_SIZE);
    BggThingsQueryParams firstPageQueryParams = new BggThingsQueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setComments(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> helper.getBggPagesRange(thing.getNumcomments())
            .flatMapSequential(page -> {
              BggThingsQueryParams queryParams = new BggThingsQueryParams();
              queryParams.setId(id.toString());
              queryParams.setComments(1);
              queryParams.setPage(page);
              queryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
              return getThing(queryParams);
            })
            .flatMapIterable(Thing::getComments)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, thing.getNumcomments()))
        );
  }

  public Mono<List<Comment>> getRatings(Integer id) {
    BggThingsQueryParams firstPageQueryParams = new BggThingsQueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setRatingcomments(1);
    firstPageQueryParams.setPage(1);
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> {
          int numPages = (int) Math.ceil((double) thing.getNumcomments() / BGG_THING_COMMENTS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                BggThingsQueryParams queryParams = new BggThingsQueryParams();
                queryParams.setId(id.toString());
                queryParams.setRatingcomments(1);
                queryParams.setPage(page);
                queryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
                return getThing(queryParams);
              })
              .flatMapIterable(Thing::getComments)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Comment>> getPagedRatings(Integer id, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_THING_COMMENTS_PAGE_SIZE);
    BggThingsQueryParams firstPageQueryParams = new BggThingsQueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setRatingcomments(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> helper.getBggPagesRange(thing.getNumcomments())
            .flatMapSequential(page -> {
              BggThingsQueryParams queryParams = new BggThingsQueryParams();
              queryParams.setId(id.toString());
              queryParams.setRatingcomments(1);
              queryParams.setPage(page);
              queryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
              return getThing(queryParams);
            })
            .flatMapIterable(Thing::getComments)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, thing.getNumcomments()))
        );
  }

  public Mono<List<Version>> getVersions(Integer id) {
    BggThingsQueryParams queryParams = new BggThingsQueryParams();
    queryParams.setId(id.toString());
    queryParams.setVersions(1);
    return getThing(queryParams)
        .map(Thing::getVersions);
  }

  public Mono<List<Video>> getVideos(Integer id) {
    BggThingsQueryParams queryParams = new BggThingsQueryParams();
    queryParams.setId(id.toString());
    queryParams.setVideos(1);
    return getThing(queryParams)
        .map(Thing::getVideos);
  }

  public Mono<List<MarketplaceListing>> getMarketplacelistings(Integer id) {
    BggThingsQueryParams queryParams = new BggThingsQueryParams();
    queryParams.setId(id.toString());
    queryParams.setMarketplace(1);
    return getThing(queryParams)
        .map(Thing::getMarketplacelistings);
  }

  private Mono<Thing> getThing(BggThingsQueryParams queryParams) {
    return thingsRepository.getThings(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Things.class))
        .map(Things::getItem)
        .flatMap(l -> l.size() == 1
            ? Mono.just(l.get(0))
            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no match found")))
        .map(thingMapper::fromBggModel);
  }

  public Mono<List<Thing>> getThings(ThingsParams params) {
    BggThingsQueryParams queryParams = thingsParamsMapper.toBggModel(params);
    return thingsRepository.getThings(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Things.class))
        .map(Things::getItem)
        .map(l -> l.stream()
            .map(thingMapper::fromBggModel)
            .collect(Collectors.toList()));

  }

  public Mono<Collection> getThings(String username, CollectionParams params) {
    BggCollectionQueryParams queryParams = collectionParamsMapper.toBggModel(params);
    queryParams.setUsername(username);
    // handle subtype bug in the BBG XML API
    if (queryParams.getSubtype() == null || queryParams.getSubtype().equals(CollectionItemSubtype.boardgame.value())) {
      queryParams.setExcludesubtype(CollectionItemSubtype.boardgameexpansion.value());
    }
    return collectionRepository.getCollection(null, queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.collection.Collection.class))
        .map(collectionMapper::fromBggModel);
  }

  public Mono<Collection> getPrivateThings(String username, String cookie, CollectionParams params) {
    BggCollectionQueryParams queryParams = collectionParamsMapper.toBggModel(params);
    queryParams.setUsername(username);
    // handle subtype bug in the BBG XML API
    if (queryParams.getSubtype() == null || queryParams.getSubtype().equals(CollectionItemSubtype.boardgame.value())) {
      queryParams.setExcludesubtype(CollectionItemSubtype.boardgameexpansion.value());
    }
    queryParams.setShowprivate(1);
    return collectionRepository.getCollection(cookie, queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.collection.Collection.class))
        .map(collectionMapper::fromBggModel);
  }

}
