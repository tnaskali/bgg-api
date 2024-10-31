package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggGeekitemsV4Repository;
import li.naska.bgg.repository.model.BggGeekitemsV4QueryParams;
import li.naska.bgg.repository.model.BggGeekitemsV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("geekitemsV4Resource")
@RequestMapping("/api/v4/geekitems")
public class GeekitemsResource {

  private final BggGeekitemsV4Repository geekitemsRepository;

  public GeekitemsResource(BggGeekitemsV4Repository geekitemsRepository) {
    this.geekitemsRepository = geekitemsRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get item",
      description =
          """
          Get item for a given id and type.
          <p>
          <i>Syntax</i> : /geekitems?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geekitems?objectid=1000&objecttype=thing
          """)
  public Mono<BggGeekitemsV4ResponseBody> getGeekitems(
      @Validated @ParameterObject BggGeekitemsV4QueryParams params) {
    return geekitemsRepository.getGeekitems(params);
  }
}
