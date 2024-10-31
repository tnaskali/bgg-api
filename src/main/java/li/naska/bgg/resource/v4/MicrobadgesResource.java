package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import li.naska.bgg.repository.BggMicrobadgesV4Repository;
import li.naska.bgg.repository.model.BggMicrobadgesV4ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("microbadgesV4Resource")
@RequestMapping("/api/v4/microbadges")
public class MicrobadgesResource {

  private final BggMicrobadgesV4Repository microbadgesRepository;

  public MicrobadgesResource(BggMicrobadgesV4Repository microbadgesRepository) {
    this.microbadgesRepository = microbadgesRepository;
  }

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
