package li.naska.bgg.resource.v2;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.repository.BggForumsRepository;
import li.naska.bgg.repository.model.BggForumQueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/forum")
public class ForumResource {

  @Autowired
  private BggForumsRepository forumsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getForumAsXml(@ParameterObject @Validated BggForumQueryParams params) {
    return forumsRepository.getForum(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getForumAsJson(@ParameterObject @Validated BggForumQueryParams params) {
    return getForumAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Forum.class));
  }

}
