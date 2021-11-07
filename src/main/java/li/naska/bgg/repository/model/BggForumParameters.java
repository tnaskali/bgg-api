package li.naska.bgg.repository.model;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;
import java.util.Optional;

@Data
public class BggForumParameters {

  private final Integer id;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", getId().toString());
    Optional.ofNullable(getPage()).map(Objects::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

}
