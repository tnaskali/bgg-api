package li.naska.bgg.graphql.data;

import com.boardgamegeek.guild.Member;

import java.util.List;

public record GuildMembersData(List<Member> members) {
}
