package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggThreadV2QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "666",
      description =
          """
          Specifies the id of the thread to retrieve.
          <p>
          <i>Syntax</i> : /thread?id={id}
          <p>
          <i>Example</i> : /thread?id=666
          """)
  private Integer id;

  @Min(1)
  @Parameter(
      example = "700",
      description =
          """
          Filters the results so that only articles with an equal or higher id than NNN will be returned.
          <p>
          <i>Syntax</i> : /thread?id={id}&minarticleid={id}
          <p>
          <i>Example</i> : /thread?id=666&minarticleid=700
          """)
  private Integer minarticleid;

  @Pattern(
      regexp =
          "^(?:[1-9]\\d)?\\d{2}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12]\\d|3[01])(?: (?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d)?$")
  @Parameter(
      example = "2002-01-01 12:00:00",
      description =
          """
          Filters the results so that only articles on the specified date or later will be returned.
          <p>
          <i>Syntax</i> : /thread?id={id}&minarticledate={date}
          <p>
          <i>Example</i> : /thread?id=666&minarticledate=2002-01-01
          <p>
          <i>Example</i> : /thread?id=666&minarticledate=2002-01-01%2012%3A00%3A00
          """)
  private String minarticledate;

  @Min(1)
  @Parameter(
      example = "100",
      description =
          """
          Limits the number of articles returned to no more than NNN.
          <p>
          <i>Syntax</i> : /thread?id={id}&count={count}
          <p>
          <i>Example</i> : /thread?id=666&count=100
          """)
  private Integer count;

  @Deprecated
  @Parameter(
      description =
          """
          <i>Not currently supported.</i>
          <p>
          <i>Syntax</i> : /thread?id={id}&username={username}
          <p>
          <i>Example</i> : /thread?id=666&username=gschloesser
          """)
  private String username;
}
