package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Thing {
  private Integer id;
  private ThingType type;
  private String name;
  private List<String> alternateNames;
  private String description;
  private String thumbnail;
  private String image;
  private Integer yearpublished;
  private LocalDate datepublished;
  private LocalDate releasedate;
  private String seriescode;
  private Integer issueindex;
  private Integer minplayers;
  private Integer maxplayers;
  private Integer playingtime;
  private Integer minplaytime;
  private Integer maxplaytime;
  private Integer minage;
  private List<ThingLink> links;
  private List<ThingVideo> videos;
  private List<ThingVersion> versions;
  private ThingStatistics stats;
  private List<ThingMarketplaceListing> marketplacelistings;

  @Data
  public static class ThingLink {
    private Integer id;
    private ThingLinkType type;
    private String value;
    private Boolean inbound;
  }

  @Data
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
  public static class ThingVersion {
    private Integer id;
    private String name;
    private List<String> alternateNames;
    private VersionType type;
    private String thumbnail;
    private String image;
    private Integer yearpublished;
    private String productcode;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal depth;
    private BigDecimal weight;
    private List<ThingVersionLink> links;

    @Data
    public static class ThingVersionLink {
      private Integer id;
      private VersionLinkType type;
      private String value;
      private Boolean inbound;
    }
  }

  @Data
  public static class ThingStatistics {
    private Integer owned;
    private Integer trading;
    private Integer wanting;
    private Integer wishing;
    private ThingRating rating;
    private ThingWeight weight;
    private List<ThingRank> ranks;
    private ThingPlayerCount playerCount;
    private ThingPlayerAge playerAge;
    private ThingLanguageDependency languageDependancy;

    @Data
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
      public static class ThingRatingComment {
        private String username;
        private BigDecimal rating;
        private String value;
      }
    }

    @Data
    public static class ThingWeight {
      private Integer numweights;
      private BigDecimal averageweight;
    }

    @Data
    public static class ThingRank {
      private Integer id;
      private RankType type;
      private Integer value;
      private String name;
      private String friendlyname;
      private BigDecimal bayesaverage;
    }

    @Data
    public static class ThingPlayerCount {
      private Integer totalvotes;
      private List<ThingSuggestedPlayerCountResult> results;

      @Data
      public static class ThingSuggestedPlayerCountResult {
        private String value;
        private Integer nbBestVotes;
        private Integer nbRecommendedVotes;
        private Integer nbNotRecommendedVotes;
      }
    }

    @Data
    public static class ThingPlayerAge {
      private Integer totalvotes;
      private List<ThingSuggestedPlayerAgeResult> results;

      @Data
      public static class ThingSuggestedPlayerAgeResult {
        private String value;
        private Integer numvotes;
      }
    }

    @Data
    public static class ThingLanguageDependency {
      private Integer totalvotes;
      private List<ThingLanguageDependencyResult> results;

      @Data
      public static class ThingLanguageDependencyResult {
        private Integer level;
        private String value;
        private Integer numvotes;
      }
    }
  }

  @Data
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
