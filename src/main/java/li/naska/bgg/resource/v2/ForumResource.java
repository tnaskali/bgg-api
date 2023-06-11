package li.naska.bgg.resource.v2;

import com.boardgamegeek.forum.Forum;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggForumV2Repository;
import li.naska.bgg.repository.model.BggForumV2QueryParams;
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

@RestController("ForumV2Resource")
@RequestMapping("/api/v2/forum")
public class ForumResource {

  @Autowired
  private BggForumV2Repository forumsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Forums",
      description = """
          You can request a list of threads in a particular forum through the XMLAPI2.
          <p>
          <i>Syntax</i> : /forum?id={id}[&page={page}]
          <p>
          <i>Example</i> : /forum?id=2003721&page=1
          """,
      externalDocs = @ExternalDocumentation(
          description = "original documentation",
          url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc6"
      )
  )
  public Mono<String> getForum(@Validated @ParameterObject BggForumV2QueryParams params,
                               ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return forumsRepository.getForum(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Forum.class));
  }

}
