package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggHotnessV4Repository;
import li.naska.bgg.repository.model.BggHotnessV4QueryParams;
import li.naska.bgg.repository.model.BggHotnessV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("HotnessV4Resource")
@RequestMapping("/api/v4/hotness")
public class HotnessResource {

  @Autowired
  private BggHotnessV4Repository hotnessRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get hotness",
      description = """
          Get hotness information.
          <p>
          <i>Syntax</i> : /hotness?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /hotness?objectid=1000&objecttype=thing
          """
  )
  public Mono<BggHotnessV4ResponseBody> getHotness(@Validated @ParameterObject BggHotnessV4QueryParams params) {
    return hotnessRepository.getHotness(params);
  }

}
