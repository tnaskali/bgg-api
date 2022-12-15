package li.naska.bgg.resource.v2;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.repository.BggForumV2Repository;
import li.naska.bgg.repository.model.BggForumV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ForumV2Resource")
@RequestMapping("/api/v2/forum")
public class ForumResource {

  @Autowired
  private BggForumV2Repository forumsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getForumAsXml(@Validated BggForumV2QueryParams params) {
    return forumsRepository.getForum(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getForumAsJson(@Validated BggForumV2QueryParams params) {
    return getForumAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Forum.class));
  }

}
