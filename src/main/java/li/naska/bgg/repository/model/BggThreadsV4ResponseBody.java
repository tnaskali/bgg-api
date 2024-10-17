package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggThreadsV4ResponseBody {
  private List<Thread> data;
  private List<Operation> operations;
  private Pagination pagination;
  private Integer totalCount;

  @Data
  @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
  public static class Thread {
    private String type;
    private Integer id;
    private String href;
    private String name;
    private Boolean pinned;
    private Boolean locked;
    private Boolean moved;
    private AssocItem assocItem;
    private Creator creator;
    private Parent parent;
    private Reactions reactions;
    private Integer numthumbs;
    private Integer numreplies;
    private ZonedDateTime postdate;
    private Post lastPost;
    private ImageSets imageSets;

    @Data
    @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
    private static class AssocItem {
      private String type;
      private Integer id;
      private String name;
      private String href;
      private String label;
      private String labelpl;
      private List<Descriptor> descriptors;
      private ImageSets imageSets;
      private Integer imageid;
      private Integer nameSortIndex;
    }

    @Data
    @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
    private static class Creator {
      private String type;
      private Integer id;
      private String name;
      private String href;
      private String label;
      private String labelpl;
      private List<Descriptor> descriptors;
      private ImageSets imageSets;
      private String username;
    }

    @Data
    @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
    private static class Parent {
      private String type;
      private Integer id;
      private String name;
      private String href;
      private String label;
      private String labelpl;
      private List<Descriptor> descriptors;
      private ImageSets imageSets;
    }

    @Data
    public static class Descriptor {
      private String name;
      private String displayValue;
    }

    @Data
    private static class Reactions {
      private Integer thumbs;
    }

    @Data
    private static class Post {
      private String href;
      private ZonedDateTime postdate;
      private Creator author;
      private List<ImageSets> imageSets;
    }
  }

  @Data
  public static class ImageSets {
    private Image square;
    private Image square100;
    private Image mediacard;
    private Image mediacard100;
  }

  @Data
  public static class Image {
    private String src;

    @JsonProperty(value = "src@2x")
    private String src_at_2x;
  }

  @Data
  public static class Operation {
    private String key;
    private String label;
    private List<Option> options;

    @JsonProperty(value = "default")
    private String _default;

    @Data
    public static class Option {
      private String label;
      private String value;
    }
  }

  @Data
  public static class Pagination {
    private Integer pageid;
    private Integer perPage;
    private Integer total;
  }
}
