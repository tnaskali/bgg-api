package li.naska.bgg.mapper;

import com.boardgamegeek.enums.ItemSubtype;
import com.boardgamegeek.enums.NameType;
import com.boardgamegeek.family.Name;
import com.boardgamegeek.plays.SubtypeValue;
import li.naska.bgg.resource.vN.model.Play;
import li.naska.bgg.resource.vN.model.Plays;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface PlaysMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"username", "userid", "page", "termsofuse"})
  @Mapping(target = "numplays", source = "total")
  @Mapping(target = "plays", source = "plaies")
  Plays fromBggModel(com.boardgamegeek.plays.Plays source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"players"})
  @Mapping(target = "players", expression = "java(getPlayers(source))")
  Play fromBggModel(com.boardgamegeek.plays.Play source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"subtypes"})
  @Mapping(target = "subtypes", expression = "java(getSubtypes(source))")
  Play.Item fromBggModel(com.boardgamegeek.plays.Item source);

  @Mapping(target = "position", source = "startposition")
  @Mapping(target = "_new", source = "new")
  Play.Player fromBggModel(com.boardgamegeek.plays.Player source);

  default List<ItemSubtype> getSubtypes(com.boardgamegeek.plays.Item source) {
    return Optional.ofNullable(source.getSubtypes())
        .map(o -> o.getSubtypes().stream()
            .map(SubtypeValue::getValue)
            .collect(Collectors.toList())
        ).orElse(null);
  }

  default List<Play.Player> getPlayers(com.boardgamegeek.plays.Play source) {
    return Optional.ofNullable(source.getPlayers())
        .map(o -> o.getPlayers().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList())
        ).orElse(null);
  }

  default List<String> getAlternatenames(com.boardgamegeek.family.Family source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.ALTERNATE)
        .map(Name::getValue)
        .collect(Collectors.toList());
  }

}
