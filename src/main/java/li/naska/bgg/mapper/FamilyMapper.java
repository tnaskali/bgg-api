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

  @BeanMapping(ignoreUnmappedSourceProperties = {"names"})
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternatenames", expression = "java(getAlternatenames(source))")
  Family fromBggModel(com.boardgamegeek.family.Family source);

  Family.Link fromBggModel(com.boardgamegeek.family.Link source);

  default Name getName(com.boardgamegeek.family.Family source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.PRIMARY)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default List<Name> getAlternatenames(com.boardgamegeek.family.Family source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.ALTERNATE)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

}
