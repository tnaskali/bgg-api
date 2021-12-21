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
  private Integer totalitems;
  private ZonedDateTime pubdate;
  private List<CollectionItem> items;

  @Data
  public static class CollectionItem {
    private Integer objectid;
    private Integer collid;
    private ItemType objecttype;
    private CollectionItemSubtype subtype;
    private String name;
    private String originalname;
    private String yearpublished;
    private String image;
    private String thumbnail;
    private CollectionItemStats stats;
    private CollectionItemStatus status;
    private Integer numplays;
    private CollectionItemPrivateInfo privateinfo;
    private CollectionItemVersion version;
    private String wantpartslist;
    private String haspartslist;
    private String wishlistcomment;

    @Data
    public static class CollectionItemPrivateInfo {
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
    public static class CollectionItemStats {
      private Integer minplayers;
      private Integer maxplayers;
      private Integer minplaytime;
      private Integer maxplaytime;
      private Integer playingtime;
      private Integer numowned;
      private CollectionItemStatsRating rating;

      @Data
      public static class CollectionItemStatsRating {
        protected String value;
        protected Integer usersrated;
        protected BigDecimal average;
        protected BigDecimal bayesaverage;
        protected BigDecimal stddev;
        protected BigDecimal median;
        protected List<CollectionItemStatsRatingRank> ranks;

        @Data
        public static class CollectionItemStatsRatingRank {
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
    public static class CollectionItemStatus {
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
    public static class CollectionItemVersion {
      private Integer id;
      private VersionType type;
      private String name;
      private List<String> alternateNames;
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
      private CollectionItemVersionPublisher publisher;
      private String other;
      private String barcode;
      private List<CollectionItemVersionLink> links;

      @Data
      public static class CollectionItemVersionPublisher {
        private Integer publisherid;
        private String value;
      }

      @Data
      public static class CollectionItemVersionLink {
        private Integer id;
        private VersionLinkType type;
        private String value;
        private Boolean inbound;
      }
    }
  }
}
