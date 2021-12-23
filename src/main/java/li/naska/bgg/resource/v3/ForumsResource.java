package li.naska.bgg.resource.v3;

import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.resource.v3.model.Forum;
import li.naska.bgg.resource.v3.model.ForumParams;
import li.naska.bgg.resource.v3.model.ForumsParams;
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

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(
      @ParameterObject @Validated ForumsParams parameters) {
    return forumsService.getForums(parameters);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forum> getForum(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ForumParams parameters) {
    return forumsService.getForum(id, parameters);
  }

}
