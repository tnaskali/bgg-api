package li.naska.bgg.service.model.plays;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Player {

  private String username;
  private Integer userid;
  private String name;
  private String position;
  private String color;
  private String score;
  private String rating;
  @JsonProperty(required = true)
  private boolean win;
  @JsonProperty(value = "new", required = true)
  private boolean _new;

}
