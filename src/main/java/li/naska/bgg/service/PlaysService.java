package li.naska.bgg.service;

import li.naska.bgg.mapper.GeekplayParamsMapper;
import li.naska.bgg.mapper.PlaysMapper;
import li.naska.bgg.mapper.PlaysParamsMapper;
import li.naska.bgg.repository.BggGeekplayV2Repository;
import li.naska.bgg.repository.BggPlaysV2Repository;
import li.naska.bgg.repository.model.BggGeekplayV3RequestBody;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
import li.naska.bgg.resource.vN.model.ItemPlaysParams;
import li.naska.bgg.resource.vN.model.Play;
import li.naska.bgg.resource.vN.model.Plays;
import li.naska.bgg.resource.vN.model.UserPlaysParams;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingHelper;
import li.naska.bgg.util.PagingParams;
import li.naska.bgg.util.XmlProcessor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class PlaysService {

  private static final int BGG_PLAYS_PAGE_SIZE = 100;

  @Autowired
  private BggPlaysV2Repository playsRepository;

  @Autowired
  private PlaysParamsMapper playsParamsMapper;

  @Autowired
  private PlaysMapper playsMapper;

  @Autowired
  private BggGeekplayV2Repository geekplaysRepository;

  @Autowired
  private GeekplayParamsMapper geekplayParamsMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<List<Play>> getUserPlays(String username, UserPlaysParams params) {
    Supplier<BggPlaysV2QueryParams> queryParamsSupplier = () -> {
      BggPlaysV2QueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setUsername(username);
      return queryParams;
    };
    return getPlays(queryParamsSupplier);
  }

  public Mono<Page<Play>> getPagedUserPlays(String username, UserPlaysParams params, PagingParams pagingParams) {
    Supplier<BggPlaysV2QueryParams> queryParamsSupplier = () -> {
      BggPlaysV2QueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setUsername(username);
      return queryParams;
    };
    return getPagedPlays(queryParamsSupplier, pagingParams);
  }

  public Mono<List<Play>> getThingPlays(Integer id, ItemPlaysParams params) {
    Supplier<BggPlaysV2QueryParams> queryParamsSupplier = () -> {
      BggPlaysV2QueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType("thing");
      return queryParams;
    };
    return getPlays(queryParamsSupplier);
  }

  public Mono<Page<Play>> getPagedThingPlays(Integer id, ItemPlaysParams params, PagingParams pagingParams) {
    Supplier<BggPlaysV2QueryParams> queryParamsSupplier = () -> {
      BggPlaysV2QueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType("thing");
      return queryParams;
    };
    return getPagedPlays(queryParamsSupplier, pagingParams);
  }

  public Mono<List<Play>> getFamilyPlays(Integer id, ItemPlaysParams params) {
    Supplier<BggPlaysV2QueryParams> queryParamsSupplier = () -> {
      BggPlaysV2QueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType("family");
      return queryParams;
    };
    return getPlays(queryParamsSupplier);
  }

  public Mono<Page<Play>> getPagedFamilyPlays(Integer id, ItemPlaysParams params, PagingParams pagingParams) {
    Supplier<BggPlaysV2QueryParams> queryParamsSupplier = () -> {
      BggPlaysV2QueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType("family");
      return queryParams;
    };
    return getPagedPlays(queryParamsSupplier, pagingParams);
  }

  private Mono<List<Play>> getPlays(Supplier<BggPlaysV2QueryParams> queryParamsSupplier) {
    BggPlaysV2QueryParams firstPageQueryParams = queryParamsSupplier.get();
    firstPageQueryParams.setPage(1);
    return getPlays(firstPageQueryParams)
        .flatMap(plays -> {
          int numPages = (int) Math.ceil((double) plays.getNumplays() / BGG_PLAYS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(plays);
                }
                BggPlaysV2QueryParams queryParams = queryParamsSupplier.get();
                queryParams.setPage(page);
                return getPlays(queryParams);
              })
              .flatMapIterable(Plays::getPlays)
              .collect(Collectors.toList());
        });
  }

  private Mono<Page<Play>> getPagedPlays(Supplier<BggPlaysV2QueryParams> queryParamsSupplier, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_PLAYS_PAGE_SIZE);
    BggPlaysV2QueryParams firstPageQueryParams = queryParamsSupplier.get();
    firstPageQueryParams.setPage(helper.getBggStartPage());
    return getPlays(firstPageQueryParams)
        .flatMap(plays -> helper.getBggPagesRange(plays.getNumplays())
            .flatMapSequential(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(plays);
              }
              BggPlaysV2QueryParams queryParams = queryParamsSupplier.get();
              queryParams.setPage(page);
              return getPlays(queryParams);
            })
            .flatMapIterable(Plays::getPlays)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, plays.getNumplays()))
        );
  }

  private Mono<Plays> getPlays(BggPlaysV2QueryParams queryParams) {
    return playsRepository.getPlays(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.plays.v2.Plays.class))
        .map(playsMapper::fromBggModel);
  }

  @SneakyThrows
  public Mono<BggGeekplayV3ResponseBody> createPrivatePlay(String cookie, Play play) {
    BggGeekplayV3RequestBody requestBody = geekplayParamsMapper.toBggModel(play);
    requestBody.setAction("save");
    requestBody.setAjax(1);
    return geekplaysRepository.updateGeekplay(cookie, requestBody);
  }

  @SneakyThrows
  public Mono<BggGeekplayV3ResponseBody> updatePrivatePlay(Integer id, String cookie, Play play) {
    if (!Objects.equals(id, play.getId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Play ID mismatch");
    }
    BggGeekplayV3RequestBody requestBody = geekplayParamsMapper.toBggModel(play);
    requestBody.setAction("save");
    requestBody.setAjax(1);
    return geekplaysRepository.updateGeekplay(cookie, requestBody);
  }

  public Mono<BggGeekplayV3ResponseBody> deletePrivatePlay(Integer id, String cookie) {
    BggGeekplayV3RequestBody requestBody = new BggGeekplayV3RequestBody();
    requestBody.setPlayid(id);
    requestBody.setAction("delete");
    requestBody.setAjax(1);
    requestBody.setFinalize(1);
    return geekplaysRepository.updateGeekplay(cookie, requestBody);
  }

}
