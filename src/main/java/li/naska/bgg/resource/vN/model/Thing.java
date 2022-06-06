package li.naska.bgg.resource.vN.model;

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
  private Name name;
  private List<Name> alternatenames;
  private String description;
  private String thumbnail;
  private String image;
  private Integer yearpublished;
  private LocalDate datepublished;
  private Integer issueindex;
  private Integer minplayers;
  private Integer maxplayers;
  private LocalDate releasedate;
  private List<Link> links;
  private String seriescode;
  private List<Poll> polls;
  private Integer playingtime;
  private Integer minplaytime;
  private Integer maxplaytime;
  private Integer minage;
  private List<Video> videos;
  private List<Version> versions;
  private Integer numcomments;
  // paged
  private List<Comment> comments;
  private Statistics statistics;
  private List<MarketplaceListing> marketplacelistings;

  @Data
  public static class Link {
    private Integer id;
    private ThingLinkType type;
    private String value;
    private Boolean inbound;
  }

  @Data
  public static class Poll {
    private String name;
    private String title;
    private Integer numvotes;
    private List<Result> results;

    @Data
    public static class Result {
      private Integer level;
      private String value;
      private Integer votes;
      private Integer votesforbest;
      private Integer votesforrecommended;
      private Integer votesfornotrecommended;
    }
  }

  @Data
  public static class Video {
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
  public static class Version {
    private Integer id;
    private Name name;
    private List<Name> alternatenames;
    private VersionType type;
    private String thumbnail;
    private String image;
    private Integer yearpublished;
    private String productcode;
    private BigDecimal width;
    private BigDecimal length;
    private BigDecimal depth;
    private BigDecimal weight;
    private List<VersionLink> links;

    @Data
    public static class VersionLink {
      private Integer id;
      private VersionLinkType type;
      private String value;
      private Boolean inbound;
    }
  }

  @Data
  public static class Comment {
    private String username;
    private BigDecimal rating;
    private String value;
  }

  @Data
  public static class Statistics {
    private Integer usersrated;
    private BigDecimal average;
    private BigDecimal bayesaverage;
    private List<Rank> ranks;
    private BigDecimal stddev;
    private BigDecimal median;
    private Integer owned;
    private Integer trading;
    private Integer wanting;
    private Integer wishing;
    private Integer numcomments;
    private Integer numweights;
    private BigDecimal averageweight;

    @Data
    public static class Rank {
      private Integer id;
      private RankType type;
      private Integer value;
      private String name;
      private String friendlyname;
      private BigDecimal bayesaverage;
    }
  }

  @Data
  public static class MarketplaceListing {
    private ZonedDateTime listdate;
    private String currency;
    private BigDecimal price;
    private String condition;
    private String notes;
    private String link;
    private String title;
  }

}
