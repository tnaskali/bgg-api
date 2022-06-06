package li.naska.bgg.mapper;

import com.boardgamegeek.enums.NameType;
import li.naska.bgg.resource.vN.model.Family;
import li.naska.bgg.resource.vN.model.Name;
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
  @Mapping(target = "alternatenames", expression = "java(getAlternatenames(source))")
  @Mapping(target = "links", source = "link")
  Family fromBggModel(com.boardgamegeek.family.Family source);

  Family.Link fromBggModel(com.boardgamegeek.family.Link source);

  default Name getName(com.boardgamegeek.family.Family source) {
    return source.getName().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default List<Name> getAlternatenames(com.boardgamegeek.family.Family source) {
    return source.getName().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

}
