package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
