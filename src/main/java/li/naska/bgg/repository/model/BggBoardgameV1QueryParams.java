package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggBoardgameV1QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Show users' comments on games (set it to 1, absent by default).
          <p>
          <i>Syntax</i> : /boardgame/{gameId}?comments=1
          <p>
          <i>Example</i> : /boardgame/35424,2860?comments=1
          """)
  private Integer comments;

  @Min(1)
  @Parameter(
      example = "1",
      description =
          """
          You can use page to increment the results (set it to the page of results you want, 1 by default).
          <p>
          <i>Syntax</i> : /boardgame/{gameId}?comments=1&page={1,}
          <p>
          <i>Example</i> : /boardgame/35424,2860?comments=1&page=1
          """)
  private Integer page;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Include game statistics (set it to 1, absent by default).
          <p>
          <i>Syntax</i> : /boardgame/{gameId}?stats=1
          <p>
          <i>Example</i> : /boardgame/2860?stats=1
          """)
  private Integer stats;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Include historical game statistics (set it to 1, absent by default).
          <p>
          Use from/end parameters to set starting and ending dates. Returns all data starting from 2006-03-18.
          <p>
          <i>Syntax</i> : /boardgame/{gameId}?historical=1
          <p>
          <i>Example</i> : /boardgame/35424,2860?historical=1
          <p>
          <i>Example</i> : /boardgame/35424,2860?historical=1&from=2009-01-01&to=2009-03-17
          """)
  private Integer historical;

  @Pattern(regexp = "^[12]\\d{3}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])$")
  @Parameter(
      example = "2006-03-18",
      description =
          """
          Set the start date to include historical data (format: YYYY-MM-DD, absent by default).
          <p>
          <i>Syntax</i> : /boardgame/{gameId}?historical=1&from={date}&to={date}
          <p>
          <i>Example</i> : /boardgame/35424,2860?historical=1&from=2009-01-01&to=2009-03-17
          """)
  private String from;

  @Pattern(regexp = "^[12]\\d{3}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])$")
  @Parameter(
      example = "2999-12-31",
      description =
          """
          Set the end date to include historical data (format: YYYY-MM-DD, absent by default).
          <p>
          <i>Syntax</i> : /boardgame/{gameId}?historical=1&from={date}&to={date}
          <p>
          <i>Example</i> : /boardgame/35424,2860?historical=1&from=2009-01-01&to=2009-03-17
          """)
  private String to;
}
