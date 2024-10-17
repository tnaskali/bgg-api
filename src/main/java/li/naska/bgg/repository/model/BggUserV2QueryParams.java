package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggUserV2QueryParams {

  @NotNull
  @Parameter(
      example = "eekspider",
      description =
          """
          Specifies the user name (only one user is requestable at a time).
          <p>
          <i>Syntax</i> : /user?name={username}
          <p>
          <i>Example</i> : /user?name=eekspider
          """)
  private String name;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Turns on optional buddies reporting. Results are paged; see page parameter.
          <p>
          <i>Syntax</i> : /user?name={username}&buddies=1[&page={page}]
          <p>
          <i>Example</i> : /user?name=eekspider&buddies=1
          <p>
          <i>Example</i> : /user?name=eekspider&buddies=1&page=2
          """)
  private Integer buddies;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Turns on optional guilds reporting. Results are paged; see page parameter.
          <p>
          <i>Syntax</i> : /user?name={username}&guilds=1[&domain={domain}]
          <p>
          <i>Example</i> : /user?name=eekspider&guilds=1
          <p>
          <i>Example</i> : /user?name=eekspider&guilds=1&page=2
          """)
  private Integer guilds;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Include the user's hot 10 list from their profile. Omitted if empty.
          <p>
          <i>Note</i> : Domain is controlled by the "domain" parameter
          <p>
          <i>Syntax</i> : /user?name={username}&hot=1[&domain={domain}]
          <p>
          <i>Example</i> : /user?name=eekspider&hot=1
          <p>
          <i>Example</i> : /user?name=eekspider&hot=1&domain=rpg
          """)
  private Integer hot;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Include the user's top 10 list from their profile. Omitted if empty.
          <p>
          <i>Note</i> : Domain is controlled by the "domain" parameter
          <p>
          <i>Syntax</i> : /user?name={username}&top=1[&domain={domain}]
          <p>
          <i>Example</i> : /user?name=eekspider&top=1
          <p>
          <i>Example</i> : /user?name=eekspider&top=1&domain=rpg
          """)
  private Integer top;

  @Pattern(regexp = "^(boardgame|puzzle|rpg|videogame)$")
  @Parameter(
      description =
          """
          Controls the domain for the users hot 10 and top 10 lists.
          <p>
          The default is "boardgame"; valid values are:
          <li/>boardgame
          <li/>rpg
          <li/>videogame
          <p>
          <i>Syntax</i> : /user?name={username}&top=1&domain={domain}
          <p>
          <i>Syntax</i> : /user?name={username}&hot=1&domain={domain}
          <p>
          <i>Syntax</i> : /user?name={username}&hot=1&top=1&domain={domain}
          <p>
          <i>Example</i> : /user?name=eekspider&top=1&domain=rpg
          <p>
          <i>Example</i> : /user?name=eekspider&hot=1&domain=rpg
          <p>
          <i>Example</i> : /user?name=eekspider&hot=1&top=1&domain=rpg
          """,
      schema = @Schema(defaultValue = "boardgame"))
  private String domain;

  @Min(1)
  @Parameter(
      description =
          """
          Specifies the page of buddy and guild results to return. The default page is 1 if you don't specify it; page size
          is 100 records (Current implementation seems to return 1000 records). The page parameter controls paging for both
          buddies and guilds list if both are specified. If a &lt;buddies&gt; or &lt;guilds&gt; node is empty, it means that you have
          requested a page higher than that needed to list all the buddies/guilds or, if you're on page 1, it means that that
          user has no buddies and is not part of any guilds.
          <p>
          <i>Syntax</i> : /user?name={username}&buddies=1&page={page}
          <p>
          <i>Syntax</i> : /user?name={username}&guilds=1&page={page}
          <p>
          <i>Syntax</i> : /user?name={username}&buddies=1&guilds=1&page={page}
          <p>
          <i>Example</i> : /user?name={username}&buddies=1&page=2
          <p>
          <i>Example</i> : /user?name={username}&guilds=1&page=2
          <p>
          <i>Example</i> : /user?name={username}&buddies=1&guilds=1&page=2
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer page;
}
