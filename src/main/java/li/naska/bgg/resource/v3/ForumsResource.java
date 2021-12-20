package li.naska.bgg.resource.v3;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.resource.v3.model.ForumParams;
import li.naska.bgg.service.ForumsService;
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
@RequestMapping("/api/v3/forums")
public class ForumsResource {

  @Autowired
  private ForumsService forumsService;

  @GetMapping(
      value = "/{id}/threads",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forum> getThreads(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ForumParams parameters) {
    return forumsService.getForum(id, parameters);
  }

}
