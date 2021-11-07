package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Data
public class BggThingsParameters {

  private final List<Integer> ids;

  private List<ObjectSubtype> type;

  private Boolean versions;

  private Boolean videos;

  private Boolean stats;

  private Boolean marketplace;

  private Boolean comments;

  private Boolean ratingcomments;

  private Integer page;

  private Integer pagesize;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", ToStringParamFunctions.BGG_INTEGER_LIST_FUNCTION.apply(getIds()));
    Optional.ofNullable(getType()).map(ToStringParamFunctions.BGG_OBJECT_SUBTYPE_LIST_FUNCTION).ifPresent(e -> map.set("type", e));
    Optional.ofNullable(getVersions()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("versions", e));
    Optional.ofNullable(getVideos()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("videos", e));
    Optional.ofNullable(getStats()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("stats", e));
    // BGG API: not currently supported
    // Optional.ofNullable(getHistorical()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("historical", e));
    Optional.ofNullable(getMarketplace()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("marketplace", e));
    Optional.ofNullable(getComments()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("comments", e));
    Optional.ofNullable(getRatingcomments()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("ratingcomments", e));
    Optional.ofNullable(getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    Optional.ofNullable(getPagesize()).map(Object::toString).ifPresent(e -> map.set("pagesize", e));
    // BGG API: not currently supported
    // Optional.ofNullable(getFrom()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("from", e));
    // Optional.ofNullable(getTo()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("to", e));
    return map;
  }

}
