package li.naska.bgg;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class BggApiApplicationTests {

	private static final String SESSION_COOKIE_REGEX = "^SessionID=([a-z0-9]+);";

	private static final Pattern SESSION_COOKIE_PATTERN = Pattern.compile(SESSION_COOKIE_REGEX);

	@Test
	void contextLoads() {
		String charSequence = "SessionID=fa6f7d9ae4eee8bf69e594ea1ca9ff4b6b0f15db; expires=Tue, 15-Sep-2020 12:47:09 GMT; Max-Age=3600; path=/; domain=.boardgamegeek.com";

		Matcher matcher = SESSION_COOKIE_PATTERN.matcher(charSequence);

		boolean found = matcher.find();

		assertThat(found).isTrue();

		String group = matcher.group(1);

		assertThat(group).isEqualTo("fa6f7d9ae4eee8bf69e594ea1ca9ff4b6b0f15db");
	}

}
