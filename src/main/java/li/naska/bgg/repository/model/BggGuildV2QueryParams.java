package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGuildV2QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "666",
      description = """
          ID of the guild you want to view.
          <p>
          <i>Syntax</i> : /guild?id={id}
          <p>
          <i>Example</i> : /guild?id=666
          """
  )
  private Integer id;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description = """
          Include member roster in the results. Member list is paged and sorted.
          <p>
          <i>Syntax</i> : /guild?id={id}&members=1
          <p>
          <i>Example</i> : /guild?id=666&members=1
          """
  )
  private Integer members;

  @Pattern(regexp = "^(username|date)$")
  @Parameter(
      example = "1",
      description = """
          Specifies how to sort the members list; default is username.
          <p>
          Valid values are:
          <li/>username
          <li/>date
          <p>
          <i>Syntax</i> : /guild?id={id}&members=1&sort={sortType}
          <p>
          <i>Example</i> : /guild?id=666&members=1&sort=date
          """,
      schema = @Schema(defaultValue = "username")
  )
  private String sort;

  @Min(1)
  @Parameter(
      example = "1",
      description = """
          The page of the members list to return. Page size is 25.
          <p>
          <i>Syntax</i> : /guild?id={id}&members=1&page={page}
          <p>
          <i>Example</i> : /guild?id=666&members=1&page=1
          """,
      schema = @Schema(defaultValue = "1")
  )
  private Integer page;

}
