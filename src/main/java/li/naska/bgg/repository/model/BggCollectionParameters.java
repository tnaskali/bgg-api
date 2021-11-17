package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class BggCollectionParameters {

  private String username;

  private Boolean version;

  private ObjectSubtype subtype;

  private ObjectSubtype excludesubtype;

  private List<Integer> id;

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate modifiedsincedate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime modifiedsincetime;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getUsername()).ifPresent(e -> map.add("username", e));
    Optional.ofNullable(getVersion()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("version", e));
    Optional.ofNullable(getBrief()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("brief", e));
    Optional.ofNullable(getStats()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("stats", e));
    // BGG API: format="comma-separated"
    Optional.ofNullable(getId()).ifPresent(l -> map.add("id", l.stream().map(Object::toString).collect(Collectors.joining(","))));
    Optional.ofNullable(getSubtype()).ifPresent(e -> map.add("subtype", e.toString()));
    Optional.ofNullable(getExcludesubtype()).ifPresent(e -> map.add("excludesubtype", e.toString()));
    Optional.ofNullable(getOwn()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("own", e));
    Optional.ofNullable(getRated()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("rated", e));
    Optional.ofNullable(getPlayed()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("played", e));
    Optional.ofNullable(getComment()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("comment", e));
    Optional.ofNullable(getTrade()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("trade", e));
    Optional.ofNullable(getWant()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("want", e));
    Optional.ofNullable(getWishlist()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("wishlist", e));
    Optional.ofNullable(getWishlistpriority()).map(Object::toString).ifPresent(e -> map.add("wishlistpriority", e));
    Optional.ofNullable(getPreordered()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("preordered", e));
    Optional.ofNullable(getWanttoplay()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("wanttoplay", e));
    Optional.ofNullable(getWanttobuy()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("wanttobuy", e));
    Optional.ofNullable(getPrevowned()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("prevowned", e));
    Optional.ofNullable(getHasparts()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("hasparts", e));
    Optional.ofNullable(getWantparts()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("wantparts", e));
    Optional.ofNullable(getMinrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.add("minrating", e));
    // BGG API: name="rating"
    Optional.ofNullable(getMaxrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.add("rating", e));
    Optional.ofNullable(getMinbggrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.add("minbggrating", e));
    // BGG API: name="bggrating"
    Optional.ofNullable(getMaxbggrating()).map(ToStringParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.add("bggrating", e));
    Optional.ofNullable(getMinplays()).ifPresent(e -> map.add("minplays", e.toString()));
    Optional.ofNullable(getMaxplays()).ifPresent(e -> map.add("maxplays", e.toString()));
    Optional.ofNullable(getShowprivate()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("showprivate", e));
    Optional.ofNullable(getCollid()).ifPresent(e -> map.add("collid", e.toString()));
    if (getModifiedsincedate() != null) {
      if (getModifiedsincetime() != null) {
        map.add("minarticledate", ToStringParamFunctions.BGG_LOCALDATETIME_FUNCTION.apply(getModifiedsincedate().atTime(getModifiedsincetime())));
      } else {
        map.add("minarticledate", ToStringParamFunctions.BGG_LOCALDATE_FUNCTION.apply(getModifiedsincedate()));
      }
    }
    return map;
  }

}
