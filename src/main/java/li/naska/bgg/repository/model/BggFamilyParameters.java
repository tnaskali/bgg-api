package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.FamilyType;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class BggFamilyParameters {

  private List<Integer> id;

  private List<FamilyType> type;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getId()).ifPresent(l -> map.add("id", l.stream().map(Object::toString).collect(Collectors.joining(","))));
    Optional.ofNullable(getType()).ifPresent(l -> map.add("type", l.stream().map(Object::toString).collect(Collectors.joining(","))));
    return map;
  }

}
