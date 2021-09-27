package li.naska.bgg.resource.v1;

import com.boardgamegeek.enums.SortType;
import com.boardgamegeek.guild.Guild;
import li.naska.bgg.repository.BggGuildsRepository;
import li.naska.bgg.repository.model.BggGuildParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/guilds")
public class GuildsResource {

  @Autowired
  private BggGuildsRepository bggGuildsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Guild> getGuild(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "members", required = false) Boolean members,
      @RequestParam(value = "sort", required = false) SortType sort,
      @RequestParam(value = "page", required = false) Integer page
  ) {
    BggGuildParameters parameters = new BggGuildParameters(id);
    parameters.setMembers(members);
    parameters.setSort(sort);
    parameters.setPage(page);
    return bggGuildsService.getGuild(parameters);
  }

}
