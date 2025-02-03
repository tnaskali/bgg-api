package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Data;

@Data
public class BggGeekaccountToplistV3RequestBody {

  @NotNull
  @Pattern(regexp = "^(?:savetoplistitem|deletetoplistitem|savetoplistorder)$")
  @Parameter(
      example = "savetoplistitem",
      description =
          """
          Action to perform.
          <p>
          Possible values are:
          <li/>savetoplistitem (append to toplist)
          <li/>deletetoplistitem (delete from toplist)
          <li/>savetoplistorder (reorder toplist)
          """)
  private String action;

  @NotNull
  @Pattern(regexp = "^(?:hot|top)$")
  @Parameter(
      example = "top",
      description =
          """
          List type.
          <p>
          Possible values are:
          <li/>hot
          <li/>top
          """)
  private String listtype;

  @NotNull
  @Pattern(regexp = "^(?:boardgame|rpg|videogame)$")
  @Parameter(
      example = "boardgame",
      description =
          """
          Domain.
          <p>
          Possible values are any domain:
          <li/>boardgame
          <li/>rpg
          <li/>videogame
          """)
  private String domain;

  @Parameter(
      description =
          """
          Internal Id of the toplistitem to delete.
          <p>
          Only relevant for "action=deletetoplistitem"
          """)
  private Integer id;

  @Parameter(
      example = "205637",
      description =
          """
          Id of the object to add to the toplist.
          <p>
          Only relevant for "action=savetoplistitem"
          """)
  private Integer objectid;

  @Pattern(
      regexp = "^(?:company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(
      example = "thing",
      description =
          """
          Type or subtype of the object to add to the toplist.
          <p>
          Only relevant for "action=savetoplistitem"
          <p>
          Possible values are any object type:
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
          Or any of their subtypes.
          """)
  private String objecttype;

  @Parameter(
      description =
          """
          New ordering of the elements of the toplist.
          <p>
          Only relevant for "action=savetoplistorder"
          """)
  private List<String> listitems;

  // unnecessary properties

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer ajax;

  // unnecessary properties

  @Deprecated
  @Pattern(regexp = "^Save$")
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "Save" in the case of action=savetoplistitem.
          """)
  private String save;

  @Deprecated
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to correspond to the object name or AKA selected in the search box in the case of action=savetoplistitem.
          """)
  private String geekitemname;

  @Deprecated
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Corresponds to the "Submit" form button and always has a value of "Submit".
          """)
  private String B1;
}
