package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"subdomains", "forums", "linkedforums"})
public class BggForumsThreadsV4ResponseBody {

  private List<Thread> threads;

  private BigDecimal elapsedtime;

  private Config config;

  @Data
  public static class Thread {

    private Integer threadid;

    private Integer shadowthreadid;

    private Integer forumid;

    private String objecttype;

    private Integer objectid;

    private Integer userid;

    private LocalDateTime postdate;

    private Integer numposts;

    private LocalDateTime lastpostdate;

    private Integer lastpostuserid;

    private Integer lastpostarticleid;

    private Integer hidden;

    private Integer pin;

    private Integer frontpin;

    private Integer locked;

    private Integer hidefrontpage;

    private String subject;

    private LocalDateTime thread_postdate;

    private LocalDateTime thread_lastpostdate;

    private Integer numrecommend;

    private String forumtitle;

    private Integer forumuid;

    private String linkname;

    private User user;

    private String lastpostusername;

    private String objectlink;

    private String forumlink;

    private String forum_href;

    private String forum_name;

    private String pagination;

    private String href;

    private String objecthref;

    private String objectname;
  }

  @Data
  public static class User {

    private String username;

    private Integer avatar;

    private String avatarfile;

    private String avatarurl_md;
  }

  @Data
  @JsonIgnoreProperties(
      value = {
        "sorttypes",
        "showforumlink",
        "showsubdomains",
        "showobjectlink",
        "showcontrols",
        "showpost",
        "showbrowse",
        "showsearch",
        "browselink",
        "browselink_newui"
      })
  public static class Config {

    private String moduletitle;

    private Integer endpage;

    private Integer article_showcount;

    private Integer forumuid;

    private Integer forumid;

    private Integer numitems;

    private Integer numthreads;

    private String forumtitle;
  }
}
