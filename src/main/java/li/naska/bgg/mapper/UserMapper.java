package li.naska.bgg.mapper;

import com.boardgamegeek.user.Buddies;
import com.boardgamegeek.user.Guilds;
import li.naska.bgg.resource.v3.model.User;
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

  User.UserBuddy fromBggModel(com.boardgamegeek.user.Buddy source);

  User.UserGuild fromBggModel(com.boardgamegeek.user.Guild source);

  @Mapping(target = "items", source = "item")
  User.UserRanking fromBggModel(com.boardgamegeek.user.Ranking source);

  User.UserRanking.UserRankingItem fromBggModel(com.boardgamegeek.user.RankingItem source);

  default Integer getNumbuddies(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getBuddies())
        .map(Buddies::getTotal)
        .orElse(null);
  }

  default List<User.UserBuddy> getBuddies(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getBuddies())
        .map(o -> o.getBuddy().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

  default Integer getNumguilds(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getGuilds())
        .map(Guilds::getTotal)
        .orElse(null);
  }

  default List<User.UserGuild> getGuilds(com.boardgamegeek.user.User source) {
    return Optional.ofNullable(source.getGuilds())
        .map(o -> o.getGuild().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
