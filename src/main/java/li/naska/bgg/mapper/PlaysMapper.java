package li.naska.bgg.mapper;

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
  Plays fromBggModel(com.boardgamegeek.plays.v2.Plays source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"players"})
  @Mapping(target = "players", expression = "java(getPlayers(source))")
  Play fromBggModel(com.boardgamegeek.plays.v2.Play source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"subtypes"})
  @Mapping(target = "subtypes", expression = "java(getSubtypes(source))")
  Play.Item fromBggModel(com.boardgamegeek.plays.v2.Item source);

  @Mapping(target = "position", source = "startposition")
  @Mapping(target = "_new", source = "new")
  Play.Player fromBggModel(com.boardgamegeek.plays.v2.Player source);

  default List<String> getSubtypes(com.boardgamegeek.plays.v2.Item source) {
    return Optional.ofNullable(source.getSubtypes())
        .map(o -> o.getSubtypes().stream()
            .map(com.boardgamegeek.plays.v2.SubtypeValue::getValue)
            .collect(Collectors.toList())
        ).orElse(null);
  }

  default List<Play.Player> getPlayers(com.boardgamegeek.plays.v2.Play source) {
    return Optional.ofNullable(source.getPlayers())
        .map(o -> o.getPlayers().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList())
        ).orElse(null);
  }

}
