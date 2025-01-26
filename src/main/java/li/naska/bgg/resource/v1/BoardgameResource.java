package li.naska.bgg.resource.v1;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Set;
import li.naska.bgg.repository.BggBoardgameV1Repository;
import li.naska.bgg.repository.model.BggBoardgameV1QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("boardgameV1Resource")
@RequestMapping("/api/v1/boardgame")
public class BoardgameResource {

  private final BggBoardgameV1Repository boardgameRepository;

  public BoardgameResource(BggBoardgameV1Repository boardgameRepository) {
    this.boardgameRepository = boardgameRepository;
  }

  @GetMapping(
      path = "/{ids}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Retrieve information about a particular game or games",
      description =
          """
          Retrieve information about a particular game or games.
          <p>
          <i>Syntax</i> : /boardgame/{ids}[?{parameters}]
          <p>
          <i>Example, single game</i> : /boardgame/35424
          <p>
          <i>Example, multiple games</i> : /boardgame/35424,2860
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc4"))
  public Mono<String> getBoardgames(
      @PathVariable @Parameter(description = "The game id(s).", example = "[ 35424, 2860 ]")
          Set<Integer> ids,
      @Validated @ParameterObject BggBoardgameV1QueryParams params,
      ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return boardgameRepository.getBoardgamesAsXml(ids, params);
    } else {
      return boardgameRepository.getBoardgamesAsJson(ids, params);
    }
  }
}
