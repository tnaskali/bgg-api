package li.naska.bgg.service;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.mapper.GeeklistParamsMapper;
import li.naska.bgg.repository.BggGeeklistsRepository;
import li.naska.bgg.repository.model.BggGeeklistQueryParams;
import li.naska.bgg.resource.v3.model.GeeklistParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GeeklistsService {

  @Autowired
  private BggGeeklistsRepository geeklistsRepository;

  @Autowired
  private GeeklistParamsMapper geeklistParamsMapper;

  public Mono<Geeklist> getGeeklist(GeeklistParams params) {
    Integer id = params.getId();
    BggGeeklistQueryParams bggParams = geeklistParamsMapper.toBggModel(params);
    return geeklistsRepository.getGeeklist(id, bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Geeklist.class));
  }

}
