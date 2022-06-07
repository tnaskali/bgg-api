package li.naska.bgg.service;

import li.naska.bgg.mapper.GeeklistMapper;
import li.naska.bgg.mapper.GeeklistParamsMapper;
import li.naska.bgg.repository.BggGeeklistV1Repository;
import li.naska.bgg.repository.model.BggGeeklistV1QueryParams;
import li.naska.bgg.resource.vN.model.Geeklist;
import li.naska.bgg.resource.vN.model.GeeklistParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GeeklistsService {

  @Autowired
  private BggGeeklistV1Repository geeklistsRepository;

  @Autowired
  private GeeklistParamsMapper geeklistParamsMapper;

  @Autowired
  private GeeklistMapper geeklistMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Geeklist> getGeeklist(Integer id, GeeklistParams params) {
    BggGeeklistV1QueryParams queryParams = geeklistParamsMapper.toBggModel(params);
    return geeklistsRepository.getGeeklist(id, queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.geeklist.Geeklist.class))
        .map(geeklistMapper::fromBggModel);
  }

}
