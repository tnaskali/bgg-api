package li.naska.bgg.resource.vN;

import li.naska.bgg.repository.BggHotV2Repository;
import li.naska.bgg.resource.vN.model.*;
import li.naska.bgg.resource.vN.model.Thing.Comment;
import li.naska.bgg.resource.vN.model.Thing.MarketplaceListing;
import li.naska.bgg.resource.vN.model.Thing.Version;
import li.naska.bgg.resource.vN.model.Thing.Video;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.ItemsService;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingParams;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/vN/things")
@Validated
public class ThingsResource {

  @Autowired
  private BggHotV2Repository bggHotItemsService;

  @Autowired
  private ItemsService itemsService;

  @Autowired
  private ThingsService thingsService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Thing>> getThings(
      @ParameterObject @Validated ThingsParams params) {
    return thingsService.getThings(params);
  }

  @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<HotItem>> getHotThings(
      @ParameterObject @Validated HotItemsParams params) {
    return itemsService.getHotItems(params);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Results> searchThings(
      @ParameterObject @Validated SearchParams params) {
    return itemsService.searchItems(params);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Thing> getThing(
      @NotNull @PathVariable Integer id) {
    return thingsService.getThing(id);
  }

  @GetMapping(value = "/{id}/versions", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Version>> getVersions(
      @NotNull @PathVariable Integer id) {
    return thingsService.getVersions(id);
  }

  @GetMapping(value = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Comment>> getComments(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated PagingParams pagingParams) {
    return thingsService.getPagedComments(id, pagingParams);
  }

  @GetMapping(value = "/{id}/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Comment>> getRatings(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated PagingParams pagingParams) {
    return thingsService.getPagedRatings(id, pagingParams);
  }

  @GetMapping(value = "/{id}/videos", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Video>> getVideos(
      @NotNull @PathVariable Integer id) {
    return thingsService.getVideos(id);
  }

  @GetMapping(value = "/{id}/marketplacelistings", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<MarketplaceListing>> getMarketplacelistings(
      @NotNull @PathVariable Integer id) {
    return thingsService.getMarketplacelistings(id);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Forum>> getForums(
      @NotNull @PathVariable Integer id) {
    return forumsService.getThingForums(id);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Play>> getPlays(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ItemPlaysParams params,
      @ParameterObject @Validated PagingParams pagingParams) {
    return playsService.getPagedThingPlays(id, params, pagingParams);
  }

}
