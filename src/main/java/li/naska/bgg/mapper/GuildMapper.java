package li.naska.bgg.mapper;

import com.boardgamegeek.guild.Members;
import li.naska.bgg.resource.vN.model.Guild;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface GuildMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"members", "termsofuse"})
  @Mapping(target = "members", expression = "java(getMembers(source))")
  @Mapping(target = "nummembers", expression = "java(getNummembers(source))")
  Guild fromBggModel(com.boardgamegeek.guild.Guild source);

  Guild.Location fromBggModel(com.boardgamegeek.guild.Location source);

  Guild.Member fromBggModel(com.boardgamegeek.guild.Member source);

  default Integer getNummembers(com.boardgamegeek.guild.Guild source) {
    return Optional.ofNullable(source.getMembers())
        .map(Members::getCount)
        .orElse(null);
  }

  default List<Guild.Member> getMembers(com.boardgamegeek.guild.Guild source) {
    return Optional.ofNullable(source.getMembers())
        .map(e -> e.getMembers().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
