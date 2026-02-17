package li.naska.bgg.graphql.data;

import com.boardgamegeek.xml.user.v2.Guild;
import java.util.List;

public record UserV2Guilds(List<Guild> guilds) {}
