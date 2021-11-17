package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.DomainType;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Data
public class BggUserParameters {

  private String username;

  private Boolean buddies;

  private Boolean guilds;

  private Boolean hot;

  private Boolean top;

  private DomainType domain;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getUsername()).ifPresent(e -> map.add("name", e));
    Optional.ofNullable(getBuddies()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("buddies", e));
    Optional.ofNullable(getGuilds()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("guilds", e));
    Optional.ofNullable(getHot()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("hot", e));
    Optional.ofNullable(getTop()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("top", e));
    Optional.ofNullable(getDomain()).ifPresent(e -> map.add("domain", e.toString()));
    Optional.ofNullable(getPage()).ifPresent(e -> map.add("page", e.toString()));
    return map;
  }

}
