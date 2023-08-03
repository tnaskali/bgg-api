package li.naska.bgg.graphql.data;

import com.boardgamegeek.user.v2.Buddy;

import java.util.List;

public record UserV2Buddies(List<Buddy> buddies) {
}
