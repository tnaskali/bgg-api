package li.naska.bgg.graphql.data;

import com.boardgamegeek.user.Buddy;

import java.util.List;

public record UserBuddiesData(List<Buddy> buddies) {
}
