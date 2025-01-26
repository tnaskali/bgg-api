package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggForumV2Repository;
import li.naska.bgg.repository.model.BggForumV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("forumV2Resource")
@RequestMapping("/api/v2/forum")
public class ForumResource {

  private final BggForumV2Repository forumRepository;

  public ForumResource(BggForumV2Repository forumRepository) {
    this.forumRepository = forumRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Forums",
      description =
          """
          You can request a list of threads in a particular forum through the XMLAPI2.
          <p>
          <i>Syntax</i> : /forum?id={id}[&page={page}]
          <p>
          <i>Example</i> : /forum?id=2003721&page=1
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc6"))
  public Mono<String> getForum(
      @Validated @ParameterObject BggForumV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return forumRepository.getForumAsXml(params);
    } else {
      return forumRepository.getForumAsJson(params);
    }
  }
}
