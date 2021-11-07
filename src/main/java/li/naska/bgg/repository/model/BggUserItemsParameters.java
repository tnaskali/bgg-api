package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Data
public class BggUserItemsParameters {

  private final String username;

  private Boolean version;

  private ObjectSubtype subtype;

  private ObjectSubtype excludesubtype;

  private List<Integer> ids;

  private Boolean brief;

  private Boolean stats;

  private Boolean own;

  private Boolean rated;

  private Boolean played;

  private Boolean comment;

  private Boolean trade;

  private Boolean want;

  private Boolean wishlist;

  private Integer wishlistpriority;

  private Boolean preordered;

  private Boolean wanttoplay;

  private Boolean wanttobuy;

  private Boolean prevowned;

  private Boolean hasparts;

  private Boolean wantparts;

  private BigDecimal minrating;

  private BigDecimal maxrating;

  private BigDecimal minbggrating;

  private BigDecimal maxbggrating;

  private Integer minplays;

  private Integer maxplays;

  private Boolean showprivate;

  private Integer collid;

  private LocalDate modifiedsincedate;

  private LocalTime modifiedsincetime;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("username", getUsername());
    Optional.ofNullable(getVersion()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("version", e));
    Optional.ofNullable(getBrief()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("brief", e));
    Optional.ofNullable(getStats()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("stats", e));
    // BGG API: format="comma-separated"
    Optional.ofNullable(getIds()).map(ToStringParamFunctions.BGG_INTEGER_LIST_FUNCTION).ifPresent(e -> map.set("id", e));
    Optional.ofNullable(getSubtype()).map(ToStringParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("subtype", e));
    Optional.ofNullable(getExcludesubtype()).map(ToStringParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("excludesubtype", e));
    Optional.ofNullable(getOwn()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("own", e));
    Optional.ofNullable(getRated()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("rated", e));
    Optional.ofNullable(getPlayed()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("played", e));
    Optional.ofNullable(getComment()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("comment", e));
    Optional.ofNullable(getTrade()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("trade", e));
    Optional.ofNullable(getWant()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("want", e));
    Optional.ofNullable(getWishlist()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wishlist", e));
    Optional.ofNullable(getWishlistpriority()).map(Object::toString).ifPresent(e -> map.set("wishlistpriority", e));
    Optional.ofNullable(getPreordered()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("preordered", e));
    Optional.ofNullable(getWanttoplay()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wanttoplay", e));
    Optional.ofNullable(getWanttobuy()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wanttobuy", e));
    Optional.ofNullable(getPrevowned()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("prevowned", e));
    Optional.ofNullable(getHasparts()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("hasparts", e));
    Optional.ofNullable(getWantparts()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wantparts", e));
    Optional.ofNullable(getMinrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("minrating", e));
    // BGG API: name="rating"
    Optional.ofNullable(getMaxrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("rating", e));
    Optional.ofNullable(getMinbggrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("minbggrating", e));
    // BGG API: name="bggrating"
    Optional.ofNullable(getMaxbggrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("bggrating", e));
    Optional.ofNullable(getMinplays()).map(Object::toString).ifPresent(e -> map.set("minplays", e));
    Optional.ofNullable(getMaxplays()).map(Object::toString).ifPresent(e -> map.set("maxplays", e));
    Optional.ofNullable(getShowprivate()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("showprivate", e));
    Optional.ofNullable(getCollid()).map(Object::toString).ifPresent(e -> map.set("collid", e));
    if (getModifiedsincedate() != null) {
      if (getModifiedsincetime() != null) {
        map.set("minarticledate", ToStringParamFunctions.BGG_LOCALDATETIME_FUNCTION.apply(getModifiedsincedate().atTime(getModifiedsincetime())));
      } else {
        map.set("minarticledate", ToStringParamFunctions.BGG_LOCALDATE_FUNCTION.apply(getModifiedsincedate()));
      }
    }
    return map;
  }

}
