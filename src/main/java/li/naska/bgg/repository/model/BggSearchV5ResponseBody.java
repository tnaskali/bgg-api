package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ItemType;
import li.naska.bgg.resource.v5.model.SearchDomain;
import lombok.Data;

import java.util.List;

@Data
public class BggSearchV5ResponseBody {

  public List<UnifiedSearchResult> items;

  @Data
  public static class UnifiedSearchResult {

    private Integer objectid;

    private SearchDomain subtype;

    private String primaryname;

    private Integer nameid;

    private Integer yearpublished;

    private String ordtitle;

    private Integer rep_imageid;

    private ItemType objecttype;

    private String name;

    private Integer sortindex;

    private String type;

    private Integer id;

    private String href;

  }

}
