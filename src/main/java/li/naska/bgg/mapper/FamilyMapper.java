package li.naska.bgg.mapper;

import com.boardgamegeek.enums.NameType;
import com.boardgamegeek.family.Name;
import li.naska.bgg.resource.v3.model.Family;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface FamilyMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"name", "link"})
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternateNames", expression = "java(getAlternateNames(source))")
  @Mapping(target = "links", source = "link")
  Family fromBggModel(com.boardgamegeek.family.Family source);

  Family.FamilyLink fromBggModel(com.boardgamegeek.family.Link source);

  default String getName(com.boardgamegeek.family.Family source) {
    return source.getName().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(Name::getValue)
        .findFirst()
        .get();
  }

  default List<String> getAlternateNames(com.boardgamegeek.family.Family source) {
    return source.getName().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(Name::getValue)
        .collect(Collectors.toList());
  }

}
