package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.DomainType;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Data
public class BggUserParameters {

  private final String username;

  private Boolean buddies;

  private Boolean guilds;

  private Boolean hot;

  private Boolean top;

  private DomainType domain;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("name", getUsername());
    Optional.ofNullable(getBuddies()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("buddies", e));
    Optional.ofNullable(getGuilds()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("guilds", e));
    Optional.ofNullable(getHot()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("hot", e));
    Optional.ofNullable(getTop()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("top", e));
    Optional.ofNullable(getDomain()).map(ToStringParamFunctions.BGG_DOMAIN_TYPE_FUNCTION).ifPresent(e -> map.set("domain", e));
    Optional.ofNullable(getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

}
