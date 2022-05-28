package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BggUnifiedSearchQueryParams {

  @NotNull
  private String q;

  @NotNull
  private Integer showcount;

}
