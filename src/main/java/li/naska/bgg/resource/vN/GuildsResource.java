package li.naska.bgg.resource.vN;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.resource.vN.model.Guild;
import li.naska.bgg.resource.vN.model.Guild.Member;
import li.naska.bgg.resource.vN.model.GuildMembersParams;
import li.naska.bgg.service.GuildsService;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vN/guilds")
public class GuildsResource {

  @Autowired
  private GuildsService guildsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Guild> getGuild(
      @NotNull @PathVariable Integer id) {
    return guildsService.getGuild(id);
  }

  @GetMapping(value = "/{id}/members", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Member>> getMembers(
      @NotNull @PathVariable Integer id,
      @Validated GuildMembersParams params,
      @Validated PagingParams pagingParams) {
    return guildsService.getPagedMembers(id, params, pagingParams);
  }

}
