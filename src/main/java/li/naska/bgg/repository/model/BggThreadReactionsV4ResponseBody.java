package li.naska.bgg.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class BggThreadReactionsV4ResponseBody {

  private Integer thumbs;

  private List<Link> links;

  private List<Integer> users;

  @Data
  public static class Link {

    private String rel;

    private String uri;

  }

}
