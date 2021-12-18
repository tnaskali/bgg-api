package li.naska.bgg.resource.v3.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GeeklistParams {

  @NotNull
  private Integer id;

  private Boolean comments;

}
