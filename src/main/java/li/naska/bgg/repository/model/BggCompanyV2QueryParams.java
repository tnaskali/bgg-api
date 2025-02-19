package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggCompanyV2QueryParams {

  @NotNull
  @Pattern(regexp = "^[1-9]\\d*(?:,[1-9]\\d*)*+$")
  @Parameter(
      example = "22924,30347",
      description =
          """
          Specifies the id of the company(ies) to retrieve. To request multiple companies with a single query, one can specify a comma-delimited list of ids.
          <p>
          <i>Syntax</i> : /company?id={ids}
          <p>
          <i>Example</i> : /company?id=22924,30347
          """)
  private String id;

  @Pattern(
      regexp =
          "^(?:bgsleevemfg|boardgamepublisher|puzzleproducer|rpgpublisher|videogamedeveloper|videogamehwmfg|videogamepublisher)(?:,(?:bgsleevemfg|boardgamepublisher|puzzleproducer|rpgpublisher|videogamedeveloper|videogamehwmfg|videogamepublisher))*$")
  @Parameter(
      example = "videogamehwmfg,videogamedeveloper",
      description =
          """
          Specifies that, regardless of the type of company asked for by id, the results are filtered by the company type(s) specified. Multiple company types can be specified in a comma-delimited list.
          <p>
          The XMLAPI2 supports companies of the following company types:
          <li/>bgsleevemfg
          <li/>boardgamepublisher
          <li/>puzzleproducer
          <li/>rpgpublisher
          <li/>videogamedeveloper
          <li/>videogamehwmfg
          <li/>videogamepublisher
          <p>
          <i>Syntax</i> : /company?id={ids}&type={personTypes}
          <p>
          <i>Example</i> : /company?id=22924,30347&type=videogamehwmfg,videogamedeveloper
          """)
  private String type;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns version info for the company.
          <p>
          <i>Syntax</i> : /company?id={ids}&versions=1
          <p>
          <i>Example</i> : /company?id=22924&versions=1
          """)
  private Integer versions;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns videos for the company.
          <p>
          <i>Syntax</i> : /company?id={ids}&videos=1
          <p>
          <i>Example</i> : /company?id=22924&videos=1
          """)
  private Integer videos;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns ranking and rating stats for the company.
          <p>
          <i>Syntax</i> : /company?id={ids}&stats=1
          <p>
          <i>Example</i> : /company?id=22924&stats=1
          """)
  private Integer stats;
}
