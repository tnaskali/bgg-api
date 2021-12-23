package li.naska.bgg.resource.v3.model;

import lombok.Data;

@Data
public class HotItem {
  protected Integer id;
  protected String name;
  protected Integer yearpublished;
  protected String thumbnail;
  protected Integer rank;
}
