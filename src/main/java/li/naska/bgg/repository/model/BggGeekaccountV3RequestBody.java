package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class BggGeekaccountV3RequestBody {

  // technical properties

  @NotNull
  @Min(1)
  @Max(1)
  private Integer ajax;
  @NotNull
  @Pattern(regexp = "^(savetoplistitem|deletetoplistitem|savetoplistorder)$")
  private String action;
  private String save;

  // business properties

  private Integer id;
  private Integer objectid;
  private String objecttype;
  private String geekitemname;
  @NotNull
  @Pattern(regexp = "^(hot|top)$")
  private String listtype;
  @NotNull
  @Pattern(regexp = "^(boardgame|rpg|videogame)$")
  private String domain;
  private List<String> listitems;

}
