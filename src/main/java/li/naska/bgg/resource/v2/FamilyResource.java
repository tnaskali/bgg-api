package li.naska.bgg.resource.v2;

import com.boardgamegeek.family.v2.Items;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggFamilyV2Repository;
import li.naska.bgg.repository.model.BggFamilyV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("FamilyV2Resource")
@RequestMapping("/api/v2/family")
public class FamilyResource {

  @Autowired private BggFamilyV2Repository familiesRepository;

  @Autowired private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Family items",
      description =
          """
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
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc4"))
  public Mono<String> getFamilies(
      @Validated @ParameterObject BggFamilyV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return familiesRepository
        .getFamilies(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Items.class));
  }
}
