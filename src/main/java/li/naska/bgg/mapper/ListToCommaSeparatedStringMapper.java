package li.naska.bgg.mapper;

import org.mapstruct.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class ListToCommaSeparatedStringMapper {

  public String asString(List<?> listValue) {
    return Optional.ofNullable(listValue).map(l -> l.stream().map(Object::toString).collect(Collectors.joining(","))).orElse(null);
  }

  public List<?> asList(String stringValue) {
    return Optional.ofNullable(stringValue).map(s -> Arrays.asList(s.split(","))).orElse(null);
  }

}
