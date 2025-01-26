package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"hasAngularLink"})
public class BggBlogpostV4ResponseBody {
  private String type;
  private Integer id;
  private String name;
  private Integer author;
  private BlogItem creator;
  private String href;
  private ZonedDateTime postdate;
  private ZonedDateTime editdate;
  private String body;
  private String status;
  private String bodyXml;
  private Boolean commentsAllowed;
  private List<String> domains;
  private String canonical_link;
  private List<BlogItem> categories;
  private List<BlogItem> linkedItems;
  private ImageSets imageSets;
  private BlogItem parent;
  private List<Link> links;

  @Data
  @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
  private static class BlogItem {
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
    private DomainsHrefs domainHrefs;

    @Data
    private static class Descriptor {
      private String name;
      private String displayValue;
    }

    @Data
    private static class DomainsHrefs {
      private String rpg;
      private String videogame;
    }
  }

  @Data
  public static class ImageSets {
    private Image square;
    private Image square100;
    private Image mediacard;
    private Image mediacard100;

    @Data
    public static class Image {
      private String src;

      @JsonProperty(value = "src@2x")
      private String src_at_2x;
    }
  }

  @Data
  public static class Link {
    private String rel;
    private String uri;
  }
}
