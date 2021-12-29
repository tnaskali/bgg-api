package li.naska.bgg.resource.v2;

import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.repository.BggForumListsRepository;
import li.naska.bgg.repository.model.BggForumsQueryParams;
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
@RequestMapping("/api/v2/forumlist")
public class ForumListResource {

  @Autowired
  private BggForumListsRepository forumListsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getForumsAsXml(@ParameterObject @Validated BggForumsQueryParams params) {
    return forumListsRepository.getForums(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getForumsAsJson(@ParameterObject @Validated BggForumsQueryParams params) {
    return getForumsAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Forums.class));
  }

}
