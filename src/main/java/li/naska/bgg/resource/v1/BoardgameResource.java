package li.naska.bgg.resource.v1;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import li.naska.bgg.repository.BggBoardgameV1Repository;
import li.naska.bgg.repository.model.BggBoardgameV1QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController("BoardgameV1Resource")
@RequestMapping("/api/v1/boardgame")
public class BoardgameResource {

  @Autowired
  private BggBoardgameV1Repository boardgameRepository;

  @GetMapping(path = "/{ids}", produces = MediaType.APPLICATION_XML_VALUE)
  @Operation(
      summary = "Retrieve information about a particular game or games",
      description = """
          Retrieve information about a particular game or games.
          <p>
          <i>Syntax</i> : /boardgame/{ids}[?{parameters}]
          <p>
          <i>Example, single game</i> : /boardgame/35424
          <p>
          <i>Example, multiple games</i> : /boardgame/35424,2860
          """,
      externalDocs = @ExternalDocumentation(
          description = "original documentation",
          url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc4"
      )
  )
  public Mono<String> getBoardgames(@PathVariable @Parameter(description = "game id(s)", example = "[ 35424, 2860 ]") Set<Integer> ids,
                                    @Validated @ParameterObject BggBoardgameV1QueryParams params) {
    return boardgameRepository.getBoardgames(ids, params);
  }

}
