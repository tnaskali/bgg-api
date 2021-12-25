package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Collection {
  private ZonedDateTime pubdate;
  private Integer totalitems;
  private List<Item> items;

  @Data
  public static class Item {
    private Integer objectid;
    private Integer collid;
    private ItemType objecttype;
    private CollectionItemSubtype subtype;
    private Name name;
    private String originalname;
    private String yearpublished;
    private String image;
    private String thumbnail;
    private Stats stats;
    private Status status;
    private Integer numplays;
    private PrivateInfo privateinfo;
    private Version version;
    private String wantpartslist;
    private String haspartslist;
    private String wishlistcomment;

    @Data
    public static class PrivateInfo {
      private String privatecomment;
      private String ppCurrency;
      private BigDecimal pricepaid;
      private String cvCurrency;
      private BigDecimal currvalue;
      private Integer quantity;
      private LocalDate acquisitiondate;
      private String acquiredfrom;
      private String inventorylocation;
    }

    @Data
    public static class Stats {
      private Integer minplayers;
      private Integer maxplayers;
      private Integer minplaytime;
      private Integer maxplaytime;
      private Integer playingtime;
      private Integer numowned;
      private Rating rating;

      @Data
      public static class Rating {
        protected String value;
        protected Integer usersrated;
        protected BigDecimal average;
        protected BigDecimal bayesaverage;
        protected BigDecimal stddev;
        protected BigDecimal median;
        protected List<Rank> ranks;

        @Data
        public static class Rank {
          protected Integer id;
          protected RankType type;
          protected String name;
          protected String friendlyname;
          protected String value;
          protected String bayesaverage;
        }
      }
    }

    @Data
    public static class Status {
      private Boolean own;
      private Boolean prevowned;
      private Boolean fortrade;
      private Boolean want;
      private Boolean wanttoplay;
      private Boolean wanttobuy;
      private Boolean wishlist;
      private Boolean preordered;
      private LocalDateTime lastmodified;
    }

    @Data
    public static class Version {
      private Integer id;
      private VersionType type;
      private Name name;
      private List<Name> alternatenames;
      private Integer yearpublished;
      private String productcode;
      private BigDecimal width;
      private BigDecimal length;
      private BigDecimal depth;
      private BigDecimal weight;
      private String thumbnail;
      private String image;
      private Integer imageid;
      private Integer year;
      private Publisher publisher;
      private String other;
      private String barcode;
      private List<VersionLink> links;

      @Data
      public static class Publisher {
        private Integer publisherid;
        private String value;
      }

      @Data
      public static class VersionLink {
        private Integer id;
        private VersionLinkType type;
        private String value;
        private Boolean inbound;
      }
    }
  }
}
