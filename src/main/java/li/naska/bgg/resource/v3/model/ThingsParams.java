package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ThingType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ThingsParams {

  @NotNull
  private List<Integer> ids;

  private List<ThingType> types;

  private Boolean versions;

  private Boolean videos;

  private Boolean stats;

  private Boolean marketplace;

  private Boolean comments;

  private Boolean ratingcomments;

  private Integer page;

  private Integer pagesize;

}
