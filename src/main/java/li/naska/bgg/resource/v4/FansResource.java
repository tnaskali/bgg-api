package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggFansV4Repository;
import li.naska.bgg.repository.model.BggFansV4QueryParams;
import li.naska.bgg.repository.model.BggFansV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("FansV4Resource")
@RequestMapping("/api/v4/fans")
public class FansResource {

  @Autowired private BggFansV4Repository fansRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get fans",
      description =
          """
          Get fans information for a given object.
          <p>
          <i>Syntax</i> : /fans?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /fans?objectid=1000&objecttype=thing
          """)
  public Mono<BggFansV4ResponseBody> getFans(
      @Validated @ParameterObject BggFansV4QueryParams params) {
    return fansRepository.getFans(params);
  }
}
