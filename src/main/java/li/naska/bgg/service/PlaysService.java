package li.naska.bgg.service;

import com.boardgamegeek.enums.ItemType;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.mapper.GeekplayParamsMapper;
import li.naska.bgg.mapper.PlaysParamsMapper;
import li.naska.bgg.repository.BggGeekplaysRepository;
import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.model.BggGeekplayRequestBody;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
import li.naska.bgg.repository.model.BggPlaysQueryParams;
import li.naska.bgg.resource.v3.model.ItemPlaysParams;
import li.naska.bgg.resource.v3.model.Play;
import li.naska.bgg.resource.v3.model.UserPlaysParams;
import li.naska.bgg.util.XmlProcessor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class PlaysService {

  @Autowired
  private BggPlaysRepository playsRepository;

  @Autowired
  private PlaysParamsMapper playsParamsMapper;

  @Autowired
  private BggGeekplaysRepository geekplaysRepository;

  @Autowired
  private GeekplayParamsMapper geekplayParamsMapper;

  public Mono<Plays> getUserPlays(String username, UserPlaysParams params) {
    BggPlaysQueryParams bggParams = playsParamsMapper.toBggModel(params);
    bggParams.setUsername(username);
    return playsRepository.getPlays(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Plays.class));
  }

  public Mono<Plays> getThingPlays(Integer id, ItemPlaysParams params) {
    BggPlaysQueryParams bggParams = playsParamsMapper.toBggModel(params);
    bggParams.setId(id);
    bggParams.setType(ItemType.thing.name());
    return playsRepository.getPlays(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Plays.class));
  }

  public Mono<Plays> getFamilyPlays(Integer id, ItemPlaysParams params) {
    BggPlaysQueryParams bggParams = playsParamsMapper.toBggModel(params);
    bggParams.setId(id);
    bggParams.setType(ItemType.family.name());
    return playsRepository.getPlays(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Plays.class));
  }

  @SneakyThrows
  public Mono<BggGeekplayResponseBody> createPrivatePlay(String cookie, Play play) {
    BggGeekplayRequestBody parameters = geekplayParamsMapper.toBggModel(play);
    parameters.setAction("save");
    parameters.setAjax(1);
    return geekplaysRepository.updateGeekplay(cookie, parameters);
  }

  @SneakyThrows
  public Mono<BggGeekplayResponseBody> updatePrivatePlay(Integer id, String cookie, Play play) {
    if (!Objects.equals(id, play.getId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Play ID mismatch");
    }
    BggGeekplayRequestBody parameters = geekplayParamsMapper.toBggModel(play);
    parameters.setAction("save");
    parameters.setAjax(1);
    parameters.setVersion(2);
    return geekplaysRepository.updateGeekplay(cookie, parameters);
  }

  public Mono<BggGeekplayResponseBody> deletePrivatePlay(Integer id, String cookie) {
    BggGeekplayRequestBody parameters = new BggGeekplayRequestBody();
    parameters.setPlayid(id);
    parameters.setAction("delete");
    parameters.setAjax(1);
    parameters.setFinalize(1);
    return geekplaysRepository.updateGeekplay(cookie, parameters);
  }

}
