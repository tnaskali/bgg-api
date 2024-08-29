package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggGeeklistV1QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Retrieve comments? (default absent, set to 1 if you want comments)
          <p>
          <i>Syntax</i> : /geeklist/{id}?comments=1
          <p>
          <i>Example</i> : /geeklist/11205?comments=1
          """)
  private Integer comments;

  // unnecessary properties

  @Deprecated
  @Min(1)
  @Parameter(
      description =
          """
          <b>This parameter is obsolete and no longer supported.</b> It was required in the original api if you wanted to retrieve all items on a geeklist longer than 150 items as that's the most that could be returned on a single api call. Currently however the geeklist xml api returns the entire geeklist (all items) in a single call.
          """)
  private Integer start;

  @Deprecated
  @Min(1)
  @Parameter(
      description =
          """
          <b>This parameter is obsolete and no longer supported.</b> It was required in the original api if you wanted to retrieve all items on a geeklist longer than 150 items as that's the most that could be returned on a single api call. Currently however the geeklist xml api returns the entire geeklist (all items) in a single call.
          """)
  private Integer count;
}
