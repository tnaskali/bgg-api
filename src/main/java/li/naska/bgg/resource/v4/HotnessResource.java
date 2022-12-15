package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggHotnessV4Repository;
import li.naska.bgg.repository.model.BggHotnessV4QueryParams;
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
  public Mono<String> getHotness(@Validated BggHotnessV4QueryParams params) {
    return hotnessRepository.getHotness(params);
  }

}
