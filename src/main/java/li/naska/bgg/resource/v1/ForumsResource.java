package li.naska.bgg.resource.v1;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.repository.BggForumsRepository;
import li.naska.bgg.repository.model.BggForumParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/forums")
public class ForumsResource {

  @Autowired
  private BggForumsRepository bggForumsService;

  @GetMapping(value = "/{id}/threads", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forum> getThreads(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "page", required = false) Integer page
  ) {
    BggForumParameters parameters = new BggForumParameters(id);
    parameters.setPage(page);
    return bggForumsService.getForum(parameters);
  }

}
