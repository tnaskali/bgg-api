package li.naska.bgg.service;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.repository.BggGeeklistRepository;
import li.naska.bgg.repository.model.BggGeeklistParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GeeklistsService {

  @Autowired
  private BggGeeklistRepository geeklistRepository;

  public Mono<Geeklist> getGeeklist(BggGeeklistParameters parameters) {
    return geeklistRepository.getGeeklist(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Geeklist.class));
  }

}
