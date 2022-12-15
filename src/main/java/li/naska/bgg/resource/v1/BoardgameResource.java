package li.naska.bgg.resource.v1;

import com.boardgamegeek.search.Items;
import li.naska.bgg.repository.BggBoardgameV1Repository;
import li.naska.bgg.repository.model.BggBoardgameV1QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController("BoardgameV1Resource")
@RequestMapping("/api/v1/boardgame")
public class BoardgameResource {

  @Autowired
  private BggBoardgameV1Repository boardgameRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(path = "/{gameids}", produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getResultsAsXml(@PathVariable List<Integer> gameids,
                                      @Validated BggBoardgameV1QueryParams params) {
    return boardgameRepository.getResults(gameids, params);
  }

  @GetMapping(path = "/{gameids}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getResultsAsJson(@PathVariable List<Integer> gameids,
                                       @Validated BggBoardgameV1QueryParams params) {
    return getResultsAsXml(gameids, params)
        .map(xml -> xmlProcessor.toJsonString(xml, Items.class));
  }

}
