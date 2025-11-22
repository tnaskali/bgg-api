package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggSubtypeinfoV4Repository;
import li.naska.bgg.repository.model.BggSubtypeinfoV4QueryParams;
import li.naska.bgg.repository.model.BggSubtypeinfoV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("subtypeinfoV4Resource")
@RequestMapping("/api/v4/subtypeinfo")
public class SubtypeinfoResource {

  private final BggSubtypeinfoV4Repository subtypeinfoV4Repository;

  public SubtypeinfoResource(BggSubtypeinfoV4Repository subtypeinfoV4Repository) {
    this.subtypeinfoV4Repository = subtypeinfoV4Repository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get subtype information", description = """
          Get subtype information.
          <p>
          <i>Syntax</i> : /subtypeinfo?subtype={type}
          <p>
          <i>Example</i> : /subtypeinfo?subtype=boardgameintegration
          """)
  public Mono<BggSubtypeinfoV4ResponseBody> getSubtypeinfo(
      @Validated @ParameterObject BggSubtypeinfoV4QueryParams params) {
    return subtypeinfoV4Repository.getSubtypeinfo(params);
  }
}
