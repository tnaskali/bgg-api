package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggForumlistV2Repository;
import li.naska.bgg.repository.model.BggForumlistV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("forumListV2Resource")
@RequestMapping("/api/v2/forumlist")
public class ForumListResource {

  private final BggForumlistV2Repository forumlistRepository;

  public ForumListResource(BggForumlistV2Repository forumlistRepository) {
    this.forumlistRepository = forumlistRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Forum Lists",
      description =
          """
          You can request a list of forums for a particular type/id through the XMLAPI2.
          <p>
          <i>Syntax</i> : /forumlist?id={id}&type={type}
          <p>
          <i>Example</i> : /forumlist?id=62408&type=family
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc5"))
  public Mono<String> getForums(
      @Validated @ParameterObject BggForumlistV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return forumlistRepository.getForumsAsXml(params);
    } else {
      return forumlistRepository.getForumsAsJson(params);
    }
  }
}
