package li.naska.bgg.graphql.model;

import java.time.ZonedDateTime;

public record GuildMember(User user, ZonedDateTime joined) {
}
