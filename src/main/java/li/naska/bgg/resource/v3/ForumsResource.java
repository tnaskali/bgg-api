package li.naska.bgg.resource.v3;

import li.naska.bgg.resource.v3.model.Forum;
import li.naska.bgg.resource.v3.model.ForumsParams;
import li.naska.bgg.resource.v3.model.Thread;
import li.naska.bgg.service.ForumsService;
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
@RequestMapping("/api/v3/forums")
public class ForumsResource {

  @Autowired
  private ForumsService forumsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Forum>> getForums(
      @ParameterObject @Validated ForumsParams params) {
    return forumsService.getForums(params);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forum> getForum(
      @NotNull @PathVariable Integer id) {
    return forumsService.getForum(id);
  }

  @GetMapping(value = "/{id}/threads", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Thread>> getThreads(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated PagingParams pagingParams) {
    return forumsService.getPagedThreads(id, pagingParams);
  }

}
