package li.naska.bgg.mapper;

import com.boardgamegeek.enums.ItemSubtype;
import com.boardgamegeek.enums.NameType;
import com.boardgamegeek.family.Name;
import com.boardgamegeek.plays.SubtypeValue;
import li.naska.bgg.resource.v3.model.Plays;
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
  @Mapping(target = "plays", source = "play")
  Plays fromBggModel(com.boardgamegeek.plays.Plays source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"players"})
  @Mapping(target = "players", expression = "java(getPlayers(source))")
  Plays.Play fromBggModel(com.boardgamegeek.plays.Play source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"subtypes"})
  @Mapping(target = "subtypes", expression = "java(getSubtypes(source))")
  Plays.Play.PlayItem fromBggModel(com.boardgamegeek.plays.Item source);

  @Mapping(target = "_new", source = "new")
  Plays.Play.PlayPlayer fromBggModel(com.boardgamegeek.plays.Player source);

  default List<ItemSubtype> getSubtypes(com.boardgamegeek.plays.Item source) {
    return Optional.ofNullable(source.getSubtypes())
        .map(o -> o.getSubtype().stream()
            .map(SubtypeValue::getValue)
            .collect(Collectors.toList())
        ).orElse(null);
  }

  default List<Plays.Play.PlayPlayer> getPlayers(com.boardgamegeek.plays.Play source) {
    return Optional.ofNullable(source.getPlayers())
        .map(o -> o.getPlayer().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList())
        ).orElse(null);
  }

  default List<String> getAlternateNames(com.boardgamegeek.family.Family source) {
    return source.getName().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(Name::getValue)
        .collect(Collectors.toList());
  }

}
