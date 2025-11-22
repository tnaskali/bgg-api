package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggGeekitemV4Repository;
import li.naska.bgg.repository.model.BggGeekitemLinkeditemsV4QueryParams;
import li.naska.bgg.repository.model.BggGeekitemLinkeditemsV4ResponseBody;
import li.naska.bgg.repository.model.BggGeekitemRecsV4QueryParams;
import li.naska.bgg.repository.model.BggGeekitemRecsV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("geekitemV4Resource")
@RequestMapping("/api/v4/geekitem")
public class GeekitemResource {

  private final BggGeekitemV4Repository geekitemRepository;

  public GeekitemResource(BggGeekitemV4Repository geekitemRepository) {
    this.geekitemRepository = geekitemRepository;
  }

  @GetMapping(path = "/linkeditems", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get linked items", description = """
          Get items linked to a given item.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation
          """)
  public Mono<BggGeekitemLinkeditemsV4ResponseBody> getLinkeditems(
      @Validated @ParameterObject BggGeekitemLinkeditemsV4QueryParams params) {
    return geekitemRepository.getLinkeditems(params);
  }

  @GetMapping(path = "/recs", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get recommandations", description = """
          Get recommandations for a given item.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&pageid={page}&showcount={count}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&pageid=1&showcount=10
          """)
  public Mono<BggGeekitemRecsV4ResponseBody> getLinkeditems(
      @Validated @ParameterObject BggGeekitemRecsV4QueryParams params) {
    return geekitemRepository.getRecs(params);
  }
}
