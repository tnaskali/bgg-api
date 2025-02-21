package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"adblock", "admin"})
public class BggCurrentUserV4ResponseBody {
  private Boolean loggedIn;
  private Integer userid;
  private String firstname;
  private String lastname;
  private String avatarfile;
  private Boolean avatar;
  private String username;
  private Boolean tweetplays;
  private Boolean bskyplays;
  private Boolean showdashboardicon;
}
