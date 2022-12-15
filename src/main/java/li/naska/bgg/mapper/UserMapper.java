package li.naska.bgg.mapper;

import com.boardgamegeek.user.Buddies;
import com.boardgamegeek.user.Guilds;
import li.naska.bgg.resource.vN.model.Guild;
import li.naska.bgg.resource.vN.model.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        IntegerValueToIntegerMapper.class,
        StringValueToStringMapper.class,
        LocalDateValueToLocalDateMapper.class
    }
)
public interface UserMapper extends BaseMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"buddies", "guilds", "termsofuse"})
  @Mapping(target = "username", source = "name")
  @Mapping(target = "numbuddies", expression = "java(getNumbuddies(source))")
  @Mapping(target = "buddies", expression = "java(getBuddies(source))")
  @Mapping(target = "numguilds", expression = "java(getNumguilds(source))")
  @Mapping(target = "guilds", expression = "java(getGuilds(source))")
  User fromBggModel(com.boardgamegeek.user.User source);

  User.Buddy fromBggModel(com.boardgamegeek.user.Buddy source);

  @Mapping(target = "created", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "website", ignore = true)
  @Mapping(target = "manager", ignore = true)
  @Mapping(target = "description", ignore = true)
  @Mapping(target = "location", ignore = true)
  @Mapping(target = "nummembers", ignore = true)
  @Mapping(target = "members", ignore = true)
  Guild fromBggModel(com.boardgamegeek.user.Guild source);

  User.Ranking fromBggModel(com.boardgamegeek.user.Ranking source);

  User.Ranking.RankedItem fromBggModel(com.boardgamegeek.user.RankedItem source);

  default Integer getNumbuddies(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getBuddies())
        .map(Buddies::getTotal)
        .orElse(null);
  }

  default List<User.Buddy> getBuddies(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getBuddies())
        .map(o -> o.getBuddies().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

  default Integer getNumguilds(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getGuilds())
        .map(Guilds::getTotal)
        .orElse(null);
  }

  default List<Guild> getGuilds(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getGuilds())
        .map(o -> o.getGuilds().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
