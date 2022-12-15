package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggCollectionV1QueryParams {

  /**
   * own=1
   * <p>
   * Collections include games that are in (or exclude for 0) a user's collection. That is, the user currently owns it.
   */
  @Min(1)
  @Max(1)
  private Integer own;

  /**
   * rated=1
   */
  @Min(1)
  @Max(1)
  private Integer rated;

  /**
   * played=1
   */
  @Min(1)
  @Max(1)
  private Integer played;

  /**
   * comment=1
   */
  @Min(1)
  @Max(1)
  private Integer comment;

  /**
   * trade=1
   */
  @Min(1)
  @Max(1)
  private Integer trade;

  /**
   * want=1
   */
  @Min(1)
  @Max(1)
  private Integer want;

  /**
   * wantintrade=1
   */
  @Min(1)
  @Max(1)
  private Integer wantintrade;

  /**
   * wishlist=1
   */
  @Min(1)
  @Max(1)
  private Integer wishlist;

  /**
   * wanttoplay=1
   */
  @Min(1)
  @Max(1)
  private Integer wanttoplay;

  /**
   * wanttobuy=1
   */
  @Min(1)
  @Max(1)
  private Integer wanttobuy;

  /**
   * prevowned=1
   */
  @Min(1)
  @Max(1)
  private Integer prevowned;

  /**
   * preordered=1
   */
  @Min(1)
  @Max(1)
  private Integer preordered;

  /**
   * hasparts=1
   */
  @Min(1)
  @Max(1)
  private Integer hasparts;

  /**
   * wantparts=1
   */
  @Min(1)
  @Max(1)
  private Integer wantparts;

  /**
   * notifycontent=1
   */
  @Min(1)
  @Max(1)
  private Integer notifycontent;

  /**
   * notifysale=1
   */
  @Min(1)
  @Max(1)
  private Integer notifysale;

  /**
   * notifyauction=1
   */
  @Min(1)
  @Max(1)
  private Integer notifyauction;

  /**
   * wishlistpriority=1
   */
  @Min(1)
  @Max(5)
  private Integer wishlistpriority;

  /**
   * minrating=1
   */
  @Min(1)
  @Max(10)
  private Integer minrating;

  /**
   * maxrating=1
   */
  @Min(1)
  @Max(10)
  private Integer maxrating;

  /**
   * minbggrating=1
   */
  @Min(1)
  @Max(10)
  private Integer minbggrating;

  /**
   * maxbggrating=1
   */
  @Min(1)
  @Max(10)
  private Integer maxbggrating;

  /**
   * minplays=1
   */
  @Min(1)
  private Integer minplays;

  /**
   * maxplays=1
   */
  @Min(1)
  private Integer maxplays;

  /**
   * showprivate=1
   */
  @Min(1)
  private Integer showprivate;

}
