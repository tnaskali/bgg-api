package li.naska.bgg.resource.vN.model;

import lombok.Data;

import java.util.List;

@Data
public class Results {
  private Integer total;
  private List<Result> items;

  @Data
  public static class Result {
    private Name name;
    private Integer yearpublished;
    private String type;
    private Integer id;

    @Data
    public static class Name {
      private String type;
      private String value;
    }
  }
}
