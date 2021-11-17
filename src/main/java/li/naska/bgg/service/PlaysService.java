package li.naska.bgg.service;

import com.boardgamegeek.geekplay.Play;
import com.boardgamegeek.plays.Plays;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.repository.BggGeekplayRepository;
import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.model.BggPlaysParameters;
import li.naska.bgg.util.XmlProcessor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PlaysService {

  @Autowired
  private BggPlaysRepository playsRepository;

  @Autowired
  private BggGeekplayRepository geekplayRepository;

  public Mono<Plays> getPlays(BggPlaysParameters parameters) {
    return playsRepository.getPlays(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Plays.class));
  }

  @SneakyThrows
  public Mono<Map<String, Object>> savePlay(String username, Integer id, Play play) {
    return geekplayRepository.savePlay(username, id, play)
        .map(body -> {
          try {
            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {
            });
          } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
          }
        });
  }

  public Mono<String> deletePlay(String username, Integer playId) {
    return geekplayRepository.deletePlay(username, playId);
  }

}
