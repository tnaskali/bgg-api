package li.naska.bgg.resource.v2;

import com.boardgamegeek.thread.v2.Thread;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggThreadV2Repository;
import li.naska.bgg.repository.model.BggThreadV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("threadV2Resource")
@RequestMapping("/api/v2/thread")
public class ThreadResource {

  private final BggThreadV2Repository threadsRepository;

  private final XmlProcessor xmlProcessor;

  public ThreadResource(BggThreadV2Repository threadsRepository, XmlProcessor xmlProcessor) {
    this.threadsRepository = threadsRepository;
    this.xmlProcessor = xmlProcessor;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Threads",
      description =
          """
          With the XMLAPI2 you can request forum threads by thread id. A thread consists of some basic information about the thread and a series of articles or individual postings.
          <p>
          <i>Syntax</i> : /thread?id={id}[&{parameters}]
          <p>
          <i>Example</i> : /thread?id=666
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc7"))
  public Mono<String> getThread(
      @Validated @ParameterObject BggThreadV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return threadsRepository
        .getThread(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Thread.class));
  }
}
