package li.naska.bgg.repository.model;

import lombok.Data;

@Data
public class BggBlogV4ResponseBody {
  private String type;
  private Integer id;
  private String name;
  private String description;
}
