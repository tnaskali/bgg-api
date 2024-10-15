package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggBlogpostsV4Repository;
import li.naska.bgg.repository.model.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("BlogpostsV4Resource")
@RequestMapping("/api/v4/blogposts")
public class BlogpostsResource {

  private final BggBlogpostsV4Repository blogpostsRepository;

  public BlogpostsResource(BggBlogpostsV4Repository blogpostsRepository) {
    this.blogpostsRepository = blogpostsRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get blogposts",
      description =
          """
          Get blogposts by object id and type.
          <p>
          <i>Syntax</i> : /blogposts?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /blogposts?objectid=1000&objecttype=thing
          """)
  public Mono<BggBlogpostsV4ResponseBody> getBlogposts(
      @Validated @ParameterObject BggBlogpostsV4QueryParams params) {
    return blogpostsRepository.getBlogposts(params);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get blogpost",
      description =
          """
          Get blogpost by id.
          <p>
          <i>Syntax</i> : /blogposts/{id}
          <p>
          <i>Example</i> : /blogposts/166224
          """)
  public Mono<BggBlogpostV4ResponseBody> getBlogpost(
      @NotNull @PathVariable @Parameter(example = "166224", description = "Blogpost id.")
          Integer id) {
    return blogpostsRepository.getBlogpost(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get blogpost reactions",
      description =
          """
          Get reactions for a given blogpost.
          <p>
          <i>Syntax</i> : /blogposts/{id}/reactions[?{parameters}]
          <p>
          <i>Example</i> : /blogposts/166224/reactions
          """)
  public Mono<BggBlogpostReactionsV4ResponseBody> getBlogpostReactions(
      @NotNull @PathVariable @Parameter(example = "166224", description = "Blogpost id.")
          Integer id,
      @Validated @ParameterObject BggBlogpostReactionsV4QueryParams params) {
    return blogpostsRepository.getBlogpostReactions(id, params);
  }

  @GetMapping(path = "/{id}/tips", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get blogpost tips",
      description =
          """
          Get tips for a given blogpost.
          <p>
          <i>Syntax</i> : /blogposts/{id}/tips[?{parameters}]
          <p>
          <i>Example</i> : /blogposts/166224/tips
          """)
  public Mono<BggBlogpostTipsV4ResponseBody> getBlogpostTips(
      @NotNull @PathVariable @Parameter(example = "166224", description = "Blogpost id.")
          Integer id,
      @Validated @ParameterObject BggBlogpostTipsV4QueryParams params) {
    return blogpostsRepository.getBlogpostTips(id, params);
  }

  @GetMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get blogpost comments",
      description =
          """
          Get comments for a given blogpost.
          <p>
          <i>Syntax</i> : /blogposts/{id}/comments[?{parameters}]
          <p>
          <i>Example</i> : /blogposts/166224/comments
          """)
  public Mono<BggBlogpostCommentsV4ResponseBody> getBlogpostComments(
      @NotNull @PathVariable @Parameter(example = "166224", description = "Blogpost id.")
          Integer id,
      @Validated @ParameterObject BggBlogpostCommentsV4QueryParams params) {
    return blogpostsRepository.getBlogpostComments(id, params);
  }
}
