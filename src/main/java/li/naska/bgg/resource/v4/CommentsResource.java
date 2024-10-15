package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggCommentsV4Repository;
import li.naska.bgg.repository.model.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("CommentsV4Resource")
@RequestMapping("/api/v4/comments")
public class CommentsResource {

  @Autowired
  private BggCommentsV4Repository commentsRepository;

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get comment",
      description =
          """
          Get comment by id.
          <p>
          <i>Syntax</i> : /comments/{id}
          <p>
          <i>Example</i> : /comments/12450439
          """)
  public Mono<BggCommentV4ResponseBody> getComment(
      @NotNull @PathVariable @Parameter(example = "12450439", description = "Comment id.")
          Integer id) {
    return commentsRepository.getComment(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get comment reactions",
      description =
          """
          Get reactions for a given comment.
          <p>
          <i>Syntax</i> : /comments/{id}/reactions[?{parameters}]
          <p>
          <i>Example</i> : /comments/12450439/reactions
          """)
  public Mono<BggCommentReactionsV4ResponseBody> getCommentReactions(
      @NotNull @PathVariable @Parameter(example = "12450439", description = "Comment id.")
          Integer id,
      @Validated @ParameterObject BggCommentReactionsV4QueryParams params) {
    return commentsRepository.getCommentReactions(id, params);
  }

  @GetMapping(path = "/{id}/tips", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get comment tips",
      description =
          """
          Get tips for a given comment.
          <p>
          <i>Syntax</i> : /comments/{id}/tips[?{parameters}]
          <p>
          <i>Example</i> : /comments/12450439/tips
          """)
  public Mono<BggCommentTipsV4ResponseBody> getCommentTips(
      @NotNull @PathVariable @Parameter(example = "12450439", description = "Comment id.")
          Integer id,
      @Validated @ParameterObject BggCommentTipsV4QueryParams params) {
    return commentsRepository.getCommentTips(id, params);
  }
}
