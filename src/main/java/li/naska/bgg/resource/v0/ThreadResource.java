package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggThreadRepository;
import li.naska.bgg.repository.model.BggThreadParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/thread")
public class ThreadResource {

  @Autowired
  private BggThreadRepository threadsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getThreadAsXml(BggThreadParameters parameters) {
    return threadsRepository.getThread(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getUserAsJson(BggThreadParameters parameters) {
    return getThreadAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
