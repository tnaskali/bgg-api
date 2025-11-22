package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggFamilyV2Repository;
import li.naska.bgg.repository.model.BggFamilyV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("familyV2Resource")
@RequestMapping("/api/v2/family")
public class FamilyResource {

  private final BggFamilyV2Repository familyRepository;

  public FamilyResource(BggFamilyV2Repository familyRepository) {
    this.familyRepository = familyRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Family items",
      description = """
          In the BGG database, more abstract or esoteric concepts are represented by something called a family.
          <p>
          The XMLAPI2 supports families of the following family types:
          <li>award
          <li>awardcategory
          <li>awardset
          <li>bgaccessoryfamily
          <li>boardgamefamily
          <li>boardgamehonor
          <li>boardgamepodcast
          <li>boardgamesubdomain
          <li>rpg
          <li>rpgfamily
          <li>rpggenre
          <li>rpghonor
          <li>rpgperiodical
          <li>rpgpodcast
          <li>rpgseries
          <li>rpgsetting
          <li>rpgsystem
          <li>videogamecharacter
          <li>videogamefranchise
          <li>videogamehonor
          <li>videogameplatform
          <li>videogameseries
          <p>
          <i>Syntax</i> : /family?id={ids}[&{parameters}]
          <p>
          <i>Example</i> : /family?id=8590,62408
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc5"))
  public Mono<String> getFamilies(
      @Validated @ParameterObject BggFamilyV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return familyRepository.getItemsAsXml(params);
    } else {
      return familyRepository.getItemsAsJson(params);
    }
  }
}
