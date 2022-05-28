package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ItemType;
import li.naska.bgg.resource.v2.model.UnifiedSearchDomain;
import lombok.Data;

import java.util.List;

@Data
public class BggUnifiedSearchResponseBody {

  public List<UnifiedSearchResult> items;

  @Data
  public static class UnifiedSearchResult {

    private Integer objectid;

    private UnifiedSearchDomain subtype;

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
