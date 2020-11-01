package li.naska.bgg.mapping;

import com.boardgamegeek.plays.Play;
import com.boardgamegeek.plays.Player;
import com.boardgamegeek.plays.Players;
import li.naska.bgg.model.plays.BggPlay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PlayMapper {

    PlayMapper INSTANCE = Mappers.getMapper(PlayMapper.class);

    @Mapping(source = "players", target = "players", qualifiedByName = "playersToPlayers")
    BggPlay mapPlay(Play input);

    BggPlay.BggPlayer mapPlayer(Player player);

    @Named("playersToPlayers")
    static List<BggPlay.BggPlayer> mapPlayers(Players input) {
        return input.getPlayer().stream().map(INSTANCE::mapPlayer).collect(Collectors.toList());
    }

}
