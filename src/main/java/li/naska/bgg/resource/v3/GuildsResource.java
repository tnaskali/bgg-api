package li.naska.bgg.resource.v3;

import li.naska.bgg.resource.v3.model.Guild;
import li.naska.bgg.resource.v3.model.Guild.Member;
import li.naska.bgg.resource.v3.model.GuildParams;
import li.naska.bgg.service.GuildsService;
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

@RestController
@RequestMapping("/api/v3/guilds")
public class GuildsResource {

  @Autowired
  private GuildsService guildsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Guild> getGuild(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated GuildParams params) {
    return guildsService.getGuild(id, params);
  }

  @GetMapping(value = "/{id}/members", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Member>> getMembers(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated PagingParams pagingParams) {
    return guildsService.getPagedMembers(id, pagingParams);
  }

}
