package li.naska.bgg.resource.v2;

import com.boardgamegeek.user.User;
import li.naska.bgg.repository.BggUserV2Repository;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
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
@RequestMapping("/api/v2/user")
public class UserResource {

  @Autowired
  private BggUserV2Repository usersRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getUserAsXml(@ParameterObject @Validated BggUserV2QueryParams params) {
    return usersRepository.getUser(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getUserAsJson(@ParameterObject @Validated BggUserV2QueryParams params) {
    return getUserAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, User.class));
  }

}
