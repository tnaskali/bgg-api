package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggForumRepository;
import li.naska.bgg.repository.model.BggForumParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/forum")
public class ForumResource {

  @Autowired
  private BggForumRepository forumsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getForumAsXml(BggForumParameters parameters) {
    return forumsRepository.getForum(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getForumAsJson(BggForumParameters parameters) {
    return getForumAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
