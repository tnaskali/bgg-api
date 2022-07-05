package li.naska.bgg.resource.v2;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.repository.BggThreadV2Repository;
import li.naska.bgg.repository.model.BggThreadV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ThreadV2Resource")
@RequestMapping("/api/v2/thread")
public class ThreadResource {

  @Autowired
  private BggThreadV2Repository threadsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getThreadAsXml(@ParameterObject @Validated BggThreadV2QueryParams params) {
    return threadsRepository.getThread(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getThreadAsJson(@ParameterObject @Validated BggThreadV2QueryParams params) {
    return getThreadAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Thread.class));
  }

}
