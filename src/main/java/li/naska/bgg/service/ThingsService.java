package li.naska.bgg.service;

import com.boardgamegeek.enums.CollectionItemSubtype;
import com.boardgamegeek.thing.Items;
import li.naska.bgg.mapper.CollectionMapper;
import li.naska.bgg.mapper.CollectionParamsMapper;
import li.naska.bgg.mapper.ThingMapper;
import li.naska.bgg.mapper.ThingsParamsMapper;
import li.naska.bgg.repository.BggCollectionV2Repository;
import li.naska.bgg.repository.BggThingV2Repository;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import li.naska.bgg.repository.model.BggThingV2QueryParams;
import li.naska.bgg.resource.vN.model.Collection;
import li.naska.bgg.resource.vN.model.CollectionParams;
import li.naska.bgg.resource.vN.model.Thing;
import li.naska.bgg.resource.vN.model.Thing.Comment;
import li.naska.bgg.resource.vN.model.Thing.MarketplaceListing;
import li.naska.bgg.resource.vN.model.Thing.Version;
import li.naska.bgg.resource.vN.model.Thing.Video;
import li.naska.bgg.resource.vN.model.ThingsParams;
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
  private BggCollectionV2Repository collectionRepository;

  @Autowired
  private CollectionParamsMapper collectionParamsMapper;

  @Autowired
  private CollectionMapper collectionMapper;

  @Autowired
  private BggThingV2Repository thingsRepository;

  @Autowired
  private ThingsParamsMapper thingsParamsMapper;

  @Autowired
  private ThingMapper thingMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Thing> getThing(Integer id) {
    BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
    queryParams.setId(id.toString());
    queryParams.setStats(1);
    return getThing(queryParams);
  }

  public Mono<List<Comment>> getComments(Integer id) {
    BggThingV2QueryParams firstPageQueryParams = new BggThingV2QueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setComments(1);
    firstPageQueryParams.setPage(1);
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> {
          int numPages = (int) Math.ceil((double) thing.getNumcomments() / BGG_THING_COMMENTS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
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
    BggThingV2QueryParams firstPageQueryParams = new BggThingV2QueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setComments(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> helper.getBggPagesRange(thing.getNumcomments())
            .flatMapSequential(page -> {
              BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
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
    BggThingV2QueryParams firstPageQueryParams = new BggThingV2QueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setRatingcomments(1);
    firstPageQueryParams.setPage(1);
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> {
          int numPages = (int) Math.ceil((double) thing.getNumcomments() / BGG_THING_COMMENTS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
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
    BggThingV2QueryParams firstPageQueryParams = new BggThingV2QueryParams();
    firstPageQueryParams.setId(id.toString());
    firstPageQueryParams.setRatingcomments(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    firstPageQueryParams.setPagesize(BGG_THING_COMMENTS_PAGE_SIZE);
    return getThing(firstPageQueryParams)
        .flatMap(thing -> helper.getBggPagesRange(thing.getNumcomments())
            .flatMapSequential(page -> {
              BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
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
    BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
    queryParams.setId(id.toString());
    queryParams.setVersions(1);
    return getThing(queryParams)
        .map(Thing::getVersions);
  }

  public Mono<List<Video>> getVideos(Integer id) {
    BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
    queryParams.setId(id.toString());
    queryParams.setVideos(1);
    return getThing(queryParams)
        .map(Thing::getVideos);
  }

  public Mono<List<MarketplaceListing>> getMarketplacelistings(Integer id) {
    BggThingV2QueryParams queryParams = new BggThingV2QueryParams();
    queryParams.setId(id.toString());
    queryParams.setMarketplace(1);
    return getThing(queryParams)
        .map(Thing::getMarketplacelistings);
  }

  private Mono<Thing> getThing(BggThingV2QueryParams queryParams) {
    return thingsRepository.getThings(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Items.class))
        .map(Items::getItems)
        .flatMap(l -> l.size() == 1
            ? Mono.just(l.get(0))
            : Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "no match found")))
        .map(thingMapper::fromBggModel);
  }

  public Mono<List<Thing>> getThings(ThingsParams params) {
    BggThingV2QueryParams queryParams = thingsParamsMapper.toBggModel(params);
    return thingsRepository.getThings(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Items.class))
        .map(Items::getItems)
        .map(l -> l.stream()
            .map(thingMapper::fromBggModel)
            .collect(Collectors.toList()));

  }

  public Mono<Collection> getThings(String username, CollectionParams params) {
    BggCollectionV2QueryParams queryParams = collectionParamsMapper.toBggModel(params);
    queryParams.setUsername(username);
    // handle subtype bug in the BBG XML API
    if (queryParams.getSubtype() == null || queryParams.getSubtype().equals(CollectionItemSubtype.BOARDGAME.value())) {
      queryParams.setExcludesubtype(CollectionItemSubtype.BOARDGAMEEXPANSION.value());
    }
    return collectionRepository.getCollection(null, queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.collection.Items.class))
        .map(collectionMapper::fromBggModel);
  }

  public Mono<Collection> getPrivateThings(String username, String cookie, CollectionParams params) {
    BggCollectionV2QueryParams queryParams = collectionParamsMapper.toBggModel(params);
    queryParams.setUsername(username);
    // handle subtype bug in the BBG XML API
    if (queryParams.getSubtype() == null || queryParams.getSubtype().equals(CollectionItemSubtype.BOARDGAME.value())) {
      queryParams.setExcludesubtype(CollectionItemSubtype.BOARDGAMEEXPANSION.value());
    }
    queryParams.setShowprivate(1);
    return collectionRepository.getCollection(cookie, queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.collection.Items.class))
        .map(collectionMapper::fromBggModel);
  }

}
