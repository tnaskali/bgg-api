package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import lombok.Data;

import java.util.List;

@Data
public class BggThingsParameters {

  private final List<Integer> ids;

  private List<ObjectSubtype> type;

  private Boolean versions;

  private Boolean videos;

  private Boolean stats;

  private Boolean marketplace;

  private Boolean comments;

  private Boolean ratingcomments;

  private Integer page;

  private Integer pagesize;

}
