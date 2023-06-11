package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggForumlistV2QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "62408",
      description = """
          Specifies the id of the type of database entry you want the forum list for. This is the id that appears in
          the address of the page when visiting a particular game in the database.
          <p>
          """
  )
  private Integer id;

  @NotNull
  @Pattern(regexp = "^(company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(
      example = "family",
      description = """
          The type of entry in the database.
          <p>
          The XMLAPI2 supports the following object types:
          <li/>company
          <li/>component
          <li/>event
          <li/>family
          <li/>media
          <li/>person
          <li/>property
          <li/>thing
          <li/>version
          <li/>weblink
          <p>
          """
  )
  private String type;

}
