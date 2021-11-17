package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class BggThingsParameters {

  private List<Integer> id;

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
    Optional.ofNullable(getId()).ifPresent(l -> map.add("id", l.stream().map(Object::toString).collect(Collectors.joining(","))));
    Optional.ofNullable(getType()).ifPresent(l -> map.add("type", l.stream().map(Object::toString).collect(Collectors.joining(","))));
    Optional.ofNullable(getVersions()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("versions", e));
    Optional.ofNullable(getVideos()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("videos", e));
    Optional.ofNullable(getStats()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("stats", e));
    // BGG API: not currently supported
    // Optional.ofNullable(getHistorical()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("historical", e));
    Optional.ofNullable(getMarketplace()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("marketplace", e));
    Optional.ofNullable(getComments()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("comments", e));
    Optional.ofNullable(getRatingcomments()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("ratingcomments", e));
    Optional.ofNullable(getPage()).ifPresent(e -> map.add("page", e.toString()));
    Optional.ofNullable(getPagesize()).ifPresent(e -> map.add("pagesize", e.toString()));
    // BGG API: not currently supported
    // Optional.ofNullable(getFrom()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.add("from", e));
    // Optional.ofNullable(getTo()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.add("to", e));
    return map;
  }

}
