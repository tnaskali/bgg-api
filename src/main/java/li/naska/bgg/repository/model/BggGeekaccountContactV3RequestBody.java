package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekaccountContactV3RequestBody {

  @NotNull
  @Pattern(regexp = "^savecontact$")
  @Parameter(
      example = "savecontact",
      description =
          """
          Action to perform.
          <p>
          Possible values are:
          <li/>savecontact (update contact information)
          """)
  private String action;

  private String firstname;

  private String lastname;

  private String streetaddr1;

  private String streetaddr2;

  private String city;

  private String state;

  private String newstate;

  private String zipcode;

  private String country;

  private String email;

  private String website;

  private String phone;

  private String xboxlive_gamertag;

  private String battlenet_account;

  private String steam_account;

  private String wii_friendcode;

  private String psn_id;

  @Deprecated
  @Pattern(regexp = "^Submit$")
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Corresponds to the "Submit" form button and always has a value of "Submit".
          """)
  @JsonProperty(value = "B1")
  private String B1;
}
