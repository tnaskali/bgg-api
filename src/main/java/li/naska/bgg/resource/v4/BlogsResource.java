package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggBlogsV4Repository;
import li.naska.bgg.repository.model.BggBlogV4ResponseBody;
import li.naska.bgg.repository.model.BggBlogsPostsV4QueryParams;
import li.naska.bgg.repository.model.BggBlogsPostsV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("blogsV4Resource")
@RequestMapping("/api/v4/blogs")
public class BlogsResource {

  private final BggBlogsV4Repository blogsRepository;

  public BlogsResource(BggBlogsV4Repository blogsRepository) {
    this.blogsRepository = blogsRepository;
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get blog", description = """
          Get blog by id.
          <p>
          <i>Syntax</i> : /blogs/{id}
          <p>
          <i>Example</i> : /blogs/12035
          """)
  public Mono<BggBlogV4ResponseBody> getArticle(
      @NotNull @PathVariable @Parameter(example = "12035", description = "Blog id.") Integer id) {
    return blogsRepository.getBlog(id);
  }

  @GetMapping(path = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get blogs posts", description = """
          Get blogs posts.
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}[&{parameters}]
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing
          """)
  public Mono<BggBlogsPostsV4ResponseBody> getBlogs(
      @Validated @ParameterObject BggBlogsPostsV4QueryParams params) {
    return blogsRepository.getBlogsPosts(params);
  }
}
