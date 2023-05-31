package li.naska.bgg.graphql.data;

import com.boardgamegeek.user.Guild;

import java.util.List;

public record UserGuildsData(List<Guild> guilds) {
}
