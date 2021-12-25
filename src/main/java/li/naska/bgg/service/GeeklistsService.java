package li.naska.bgg.service;

import li.naska.bgg.mapper.GeeklistMapper;
import li.naska.bgg.mapper.GeeklistParamsMapper;
import li.naska.bgg.repository.BggGeeklistsRepository;
import li.naska.bgg.repository.model.BggGeeklistQueryParams;
import li.naska.bgg.resource.v3.model.Geeklist;
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

  @Autowired
  private GeeklistMapper geeklistMapper;

  public Mono<Geeklist> getGeeklist(Integer id, GeeklistParams params) {
    BggGeeklistQueryParams bggParams = geeklistParamsMapper.toBggModel(params);
    return geeklistsRepository.getGeeklist(id, bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(com.boardgamegeek.geeklist.Geeklist.class))
        .map(geeklistMapper::fromBggModel);
  }

}
