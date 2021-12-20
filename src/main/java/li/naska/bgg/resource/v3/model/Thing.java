package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Data
@JsonInclude(NON_EMPTY)
public class Thing {
  private Integer id;
  private ThingType type;
  private String thumbnail;
  private String name;
  private List<String> alternateNames;
  private String description;
  private String image;
  private Integer yearpublished;
  private LocalDate datepublished;
  private LocalDate releasedate;
  private List<ThingLink> links;
  private String seriescode;
  private Integer issueindex;
  private Integer minplayers;
  private Integer maxplayers;
  private Integer playingtime;
  private Integer minplaytime;
  private Integer maxplaytime;
  private Integer minage;
  private List<ThingVideo> videos;
  private List<ThingVersion> versions;
  private ThingStatistics stats;
  private List<ThingMarketplaceListing> marketplacelistings;

  @Data
  @JsonInclude(NON_EMPTY)
  public static class ThingLink {
    private Integer id;
    private ThingLinkType type;
    private String value;
    private LinkDirection direction;
  }

  @Data
  @JsonInclude(NON_EMPTY)
  public static class ThingVideo {
    private Integer id;
    private String title;
    private String category;
    private String language;
    private String link;
    private String username;
    private Integer userid;
    private ZonedDateTime postdate;
  }

  @Data
  @JsonInclude(NON_EMPTY)
  public static class ThingVersion {
    private Integer id;
    private VersionType type;
    private String thumbnail;
    private String image;
    private String name;
    private List<String> alternateNames;
    private List<ThingVersionLink> links;
    private Integer yearpublished;
    private String productcode;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal depth;
    private BigDecimal weight;

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingVersionLink {
      private Integer id;
      private VersionLinkType type;
      private String value;
      private LinkDirection direction;
    }
  }

  @Data
  @JsonInclude(NON_EMPTY)
  public static class ThingStatistics {
    private Integer owned;
    private Integer trading;
    private Integer wanting;
    private Integer wishing;
    private ThingRating rating;
    private List<ThingRank> ranks;
    private ThingWeight weight;
    private ThingPlayerCount playerCount;
    private ThingPlayerAge playerAge;
    private ThingLanguageDependency languageDependancy;

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingRating {
      private Integer usersrated;
      private BigDecimal average;
      private BigDecimal bayesaverage;
      private BigDecimal stddev;
      private BigDecimal median;
      private Integer numcomments;
      private Integer totalcomments;
      // paged
      private List<ThingRatingComment> comments;

      @Data
      @JsonInclude(NON_EMPTY)
      public static class ThingRatingComment {
        private String username;
        private BigDecimal rating;
        private String value;
      }
    }

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingRank {
      private Integer id;
      private RankType type;
      private Integer value;
      private String name;
      private String friendlyname;
      private BigDecimal bayesaverage;
    }

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingWeight {
      private Integer numweights;
      private BigDecimal averageweight;
    }

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingPlayerCount {
      private Integer totalvotes;
      private List<ThingSuggestedPlayerCountResult> results;

      @Data
      @JsonInclude(NON_EMPTY)
      public static class ThingSuggestedPlayerCountResult {
        private String value;
        private Integer nbBestVotes;
        private Integer nbRecommendedVotes;
        private Integer nbNotRecommendedVotes;
      }
    }

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingPlayerAge {
      private Integer totalvotes;
      private List<ThingSuggestedPlayerAgeResult> results;

      @Data
      @JsonInclude(NON_EMPTY)
      public static class ThingSuggestedPlayerAgeResult {
        private String value;
        private Integer numvotes;
      }
    }

    @Data
    @JsonInclude(NON_EMPTY)
    public static class ThingLanguageDependency {
      private Integer totalvotes;
      private List<ThingLanguageDependencyResult> results;

      @Data
      @JsonInclude(NON_EMPTY)
      public static class ThingLanguageDependencyResult {
        private Integer level;
        private String value;
        private Integer numvotes;
      }
    }
  }

  @Data
  @JsonInclude(NON_EMPTY)
  public static class ThingMarketplaceListing {
    private ZonedDateTime listdate;
    private String currency;
    private BigDecimal price;
    private String condition;
    private String notes;
    private String link;
    private String title;
  }
}
