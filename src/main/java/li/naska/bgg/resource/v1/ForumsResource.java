package li.naska.bgg.resource.v1;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.repository.model.BggForumParameters;
import li.naska.bgg.service.ForumsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/forums")
public class ForumsResource {

  @Autowired
  private ForumsService forumsService;

  @GetMapping(value = "/{id}/threads", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forum> getThreads(BggForumParameters parameters) {
    return forumsService.getForum(parameters);
  }

}
