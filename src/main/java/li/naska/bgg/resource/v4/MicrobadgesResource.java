package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import li.naska.bgg.repository.BggMicrobadgesV4Repository;
import li.naska.bgg.repository.model.BggMicrobadgesV4ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("MicrobadgesV4Resource")
@RequestMapping("/api/v4/microbadges")
public class MicrobadgesResource {

  @Autowired private BggMicrobadgesV4Repository microbadgesRepository;

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get microbadge by id",
      description =
          """
          Get microbadge by id.
          <p>
          <i>Syntax</i> : /microbadges/{id}
          <p>
          <i>Example</i> : /microbadges/10937
          """)
  public Mono<BggMicrobadgesV4ResponseBody> getMicrobadge(
      @PathVariable @Parameter(example = "766", description = "Microbadge id.") Integer id) {
    return microbadgesRepository.getMicrobadge(id);
  }
}
