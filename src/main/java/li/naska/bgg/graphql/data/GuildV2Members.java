package li.naska.bgg.graphql.data;

import com.boardgamegeek.guild.v2.Member;

import java.util.List;

public record GuildV2Members(List<Member> members) {
}
