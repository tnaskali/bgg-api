package li.naska.bgg.resource.v5;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggItemV5Repository;
import li.naska.bgg.repository.model.BggItemWeblinksV5QueryParams;
import li.naska.bgg.repository.model.BggItemWeblinksV5ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController("ItemV5Resource")
@RequestMapping("/api/v5/item")
public class ItemResource {

  private final BggItemV5Repository itemRepository;

  public ItemResource(BggItemV5Repository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @GetMapping(path = "/weblinks", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get item weblinks",
      description =
          """
          Get item weblinks.
          <p>
          <i>Syntax</i> : /item/weblinks?objectid={objectid}&objecttype={objecttype}&showcount={showcount}[&{parameters}]
          <p>
          <i>Example</i> : /item/boardgame?objectid=261980&objecttype=thing&showcount=1
          """)
  public Mono<BggItemWeblinksV5ResponseBody> getItemWeblinks(
      @Validated @ParameterObject BggItemWeblinksV5QueryParams params) {
    return itemRepository.getItemWeblinks(params);
  }
}
