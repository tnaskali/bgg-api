package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class BggBlogsPostsV4ResponseBody {
  private List<Post> posts;
  private Config config;

  @Data
  private static class Post {
    private String href;
    private String title;
    private Integer postid;
    private Integer numpositive;
    private LocalDate publishdate;
    private Integer numcomments;
    private String blogger;
    private String blog;
    private User user;

    @Data
    private static class User {
      private String username;
      private Integer avatar;
      private String avatarfile;
      private String avatarurl_md;
    }
  }

  @Data
  @JsonIgnoreProperties(value = "sorttypes")
  public static class Config {
    private Integer numitems;
    private Integer endpage;
  }
}
