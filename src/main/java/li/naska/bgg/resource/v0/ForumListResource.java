package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggForumlistRepository;
import li.naska.bgg.repository.model.BggForumsParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/forumlist")
public class ForumListResource {

  @Autowired
  private BggForumlistRepository forumListsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getForumsAsXml(BggForumsParameters parameters) {
    return forumListsRepository.getForums(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getForumsAsJson(BggForumsParameters parameters) {
    return getForumsAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
