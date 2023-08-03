package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = { "customurl", "url", "links" })
public class BggMicrobadgesV4ResponseBody {

  private Integer badgeid;

  private String name;

  private String title;

  private String src;

}
