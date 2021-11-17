package li.naska.bgg.repository.model;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Data
public class BggForumParameters {

  private Integer id;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getId()).ifPresent(e -> map.add("id", e.toString()));
    Optional.ofNullable(getPage()).ifPresent(e -> map.add("page", e.toString()));
    return map;
  }

}
