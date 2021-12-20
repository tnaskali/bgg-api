package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ThingType;
import lombok.Data;

import java.util.List;

@Data
public class ThingParams {

  private List<ThingType> type;

  private Boolean versions;

  private Boolean videos;

  private Boolean stats;

  private Boolean marketplace;

  private Boolean comments;

  private Boolean ratingcomments;

  private Integer page;

  private Integer pagesize;

}
