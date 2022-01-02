package li.naska.bgg.service;

import com.boardgamegeek.enums.ItemType;
import li.naska.bgg.mapper.GeekplayParamsMapper;
import li.naska.bgg.mapper.PlaysMapper;
import li.naska.bgg.mapper.PlaysParamsMapper;
import li.naska.bgg.repository.BggGeekplaysRepository;
import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.model.BggGeekplayRequestBody;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
import li.naska.bgg.repository.model.BggPlaysQueryParams;
import li.naska.bgg.resource.v3.model.ItemPlaysParams;
import li.naska.bgg.resource.v3.model.Play;
import li.naska.bgg.resource.v3.model.Plays;
import li.naska.bgg.resource.v3.model.UserPlaysParams;
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
  private BggPlaysRepository playsRepository;

  @Autowired
  private PlaysParamsMapper playsParamsMapper;

  @Autowired
  private PlaysMapper playsMapper;

  @Autowired
  private BggGeekplaysRepository geekplaysRepository;

  @Autowired
  private GeekplayParamsMapper geekplayParamsMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<List<Play>> getUserPlays(String username, UserPlaysParams params) {
    Supplier<BggPlaysQueryParams> queryParamsSupplier = () -> {
      BggPlaysQueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setUsername(username);
      return queryParams;
    };
    return getPlays(queryParamsSupplier);
  }

  public Mono<Page<Play>> getPagedUserPlays(String username, UserPlaysParams params, PagingParams pagingParams) {
    Supplier<BggPlaysQueryParams> queryParamsSupplier = () -> {
      BggPlaysQueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setUsername(username);
      return queryParams;
    };
    return getPagedPlays(queryParamsSupplier, pagingParams);
  }

  public Mono<List<Play>> getThingPlays(Integer id, ItemPlaysParams params) {
    Supplier<BggPlaysQueryParams> queryParamsSupplier = () -> {
      BggPlaysQueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType(ItemType.thing.name());
      return queryParams;
    };
    return getPlays(queryParamsSupplier);
  }

  public Mono<Page<Play>> getPagedThingPlays(Integer id, ItemPlaysParams params, PagingParams pagingParams) {
    Supplier<BggPlaysQueryParams> queryParamsSupplier = () -> {
      BggPlaysQueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType(ItemType.thing.name());
      return queryParams;
    };
    return getPagedPlays(queryParamsSupplier, pagingParams);
  }

  public Mono<List<Play>> getFamilyPlays(Integer id, ItemPlaysParams params) {
    Supplier<BggPlaysQueryParams> queryParamsSupplier = () -> {
      BggPlaysQueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType(ItemType.family.name());
      return queryParams;
    };
    return getPlays(queryParamsSupplier);
  }

  public Mono<Page<Play>> getPagedFamilyPlays(Integer id, ItemPlaysParams params, PagingParams pagingParams) {
    Supplier<BggPlaysQueryParams> queryParamsSupplier = () -> {
      BggPlaysQueryParams queryParams = playsParamsMapper.toBggModel(params);
      queryParams.setId(id);
      queryParams.setType(ItemType.family.name());
      return queryParams;
    };
    return getPagedPlays(queryParamsSupplier, pagingParams);
  }

  private Mono<List<Play>> getPlays(Supplier<BggPlaysQueryParams> queryParamsSupplier) {
    BggPlaysQueryParams firstPageQueryParams = queryParamsSupplier.get();
    firstPageQueryParams.setPage(1);
    return getPlays(firstPageQueryParams)
        .flatMap(plays -> {
          int numPages = (int) Math.ceil((double) plays.getNumplays() / BGG_PLAYS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(plays);
                }
                BggPlaysQueryParams queryParams = queryParamsSupplier.get();
                queryParams.setPage(page);
                return getPlays(queryParams);
              })
              .flatMapIterable(Plays::getPlays)
              .collect(Collectors.toList());
        });
  }

  private Mono<Page<Play>> getPagedPlays(Supplier<BggPlaysQueryParams> queryParamsSupplier, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_PLAYS_PAGE_SIZE);
    BggPlaysQueryParams firstPageQueryParams = queryParamsSupplier.get();
    firstPageQueryParams.setPage(helper.getBggStartPage());
    return getPlays(firstPageQueryParams)
        .flatMap(plays -> Flux.range(helper.getBggStartPage(), helper.getBggPages())
            .flatMapSequential(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(plays);
              }
              BggPlaysQueryParams queryParams = queryParamsSupplier.get();
              queryParams.setPage(page);
              return getPlays(queryParams);
            })
            .flatMapIterable(Plays::getPlays)
            .collect(Collectors.toList())
            .map(list -> Page.of(
                pagingParams.getPage(),
                (int) Math.ceil((double) plays.getNumplays() / pagingParams.getSize()),
                pagingParams.getSize(),
                plays.getNumplays(),
                list.subList(
                    Math.min(
                        helper.getResultStartIndex(),
                        list.size()),
                    Math.min(
                        helper.getResultEndIndex(),
                        list.size())))
            ));
  }

  private Mono<Plays> getPlays(BggPlaysQueryParams queryParams) {
    return playsRepository.getPlays(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.plays.Plays.class))
        .map(playsMapper::fromBggModel);
  }

  @SneakyThrows
  public Mono<BggGeekplayResponseBody> createPrivatePlay(String cookie, Play play) {
    BggGeekplayRequestBody requestBody = geekplayParamsMapper.toBggModel(play);
    requestBody.setAction("save");
    requestBody.setAjax(1);
    return geekplaysRepository.updateGeekplay(cookie, requestBody);
  }

  @SneakyThrows
  public Mono<BggGeekplayResponseBody> updatePrivatePlay(Integer id, String cookie, Play play) {
    if (!Objects.equals(id, play.getId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Play ID mismatch");
    }
    BggGeekplayRequestBody requestBody = geekplayParamsMapper.toBggModel(play);
    requestBody.setAction("save");
    requestBody.setAjax(1);
    requestBody.setVersion(2);
    return geekplaysRepository.updateGeekplay(cookie, requestBody);
  }

  public Mono<BggGeekplayResponseBody> deletePrivatePlay(Integer id, String cookie) {
    BggGeekplayRequestBody requestBody = new BggGeekplayRequestBody();
    requestBody.setPlayid(id);
    requestBody.setAction("delete");
    requestBody.setAjax(1);
    requestBody.setFinalize(1);
    return geekplaysRepository.updateGeekplay(cookie, requestBody);
  }

}
