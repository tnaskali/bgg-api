package li.naska.bgg.resource.v2;

import com.boardgamegeek.guild.Guild;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggGuildV2Repository;
import li.naska.bgg.repository.model.BggGuildV2QueryParams;
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

@RestController("GuildV2Resource")
@RequestMapping("/api/v2/guild")
public class GuildResource {

  @Autowired
  private BggGuildV2Repository guildsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Guilds",
      description = """
          Request information about particular guilds.
          <p>
          <i>Syntax</i> : /guild?id={id}[&{parameters}]
          <p>
          <i>Example</i> : /guild?id=666
          """,
      externalDocs = @ExternalDocumentation(
          description = "original documentation",
          url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc9"
      )
  )
  public Mono<String> getGuild(@Validated @ParameterObject BggGuildV2QueryParams params,
                               ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return guildsRepository.getGuild(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Guild.class));
  }

}
