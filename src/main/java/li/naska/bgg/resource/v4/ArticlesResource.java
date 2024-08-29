package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggArticlesV4Repository;
import li.naska.bgg.repository.model.BggArticleV4ResponseBody;
import li.naska.bgg.repository.model.BggArticlesV4QueryParams;
import li.naska.bgg.repository.model.BggArticlesV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ArticlesV4Resource")
@RequestMapping("/api/v4/articles")
public class ArticlesResource {

  @Autowired
  private BggArticlesV4Repository articlesRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get articles",
      description =
          """
          Get article by thread id.
          <p>
          <i>Syntax</i> : /articles?threadid={id}[&{parameters}]
          <p>
          <i>Example</i> : /articles?threadid=4539817
          """)
  public Mono<BggArticlesV4ResponseBody> getArticles(
      @Validated @ParameterObject BggArticlesV4QueryParams params) {
    return articlesRepository.getArticles(params);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get article",
      description =
          """
          Get article by id.
          <p>
          <i>Syntax</i> : /articles/{id}
          <p>
          <i>Example</i> : /articles/4539817
          """)
  public Mono<BggArticleV4ResponseBody> getArticle(
      @NotNull @PathVariable @Parameter(example = "4539817", description = "Article id.")
          Integer id) {
    return articlesRepository.getArticle(id);
  }
}
