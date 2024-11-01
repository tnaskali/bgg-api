package li.naska.bgg.repository.model;

import java.util.List;
import li.naska.bgg.resource.v5.model.SearchContext;
import lombok.Data;

@Data
public class BggSearchV5ResponseBody {
  private List<UnifiedSearchResult> items;

  @Data
  public static class UnifiedSearchResult {
    private Integer objectid;
    private SearchContext subtype;
    private String primaryname;
    private Integer nameid;
    private Integer yearpublished;
    private String ordtitle;
    private Integer rep_imageid;
    private String objecttype;
    private String name;
    private Integer sortindex;
    private String type;
    private Integer id;
    private String href;
  }
}
