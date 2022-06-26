package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggArticlesV4Repository;
import li.naska.bgg.repository.model.BggArticlesV4QueryParams;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController("ArticlesV4Resource")
@RequestMapping("/api/v4/articles")
public class ArticlesResource {

  @Autowired
  private BggArticlesV4Repository articlesRepository;

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getArticle(@NotNull @PathVariable Integer id) {
    return articlesRepository.getArticle(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getArticles(@ParameterObject @Validated BggArticlesV4QueryParams params) {
    return articlesRepository.getArticles(params);
  }

}
