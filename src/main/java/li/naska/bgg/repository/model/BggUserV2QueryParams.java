package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggUserV2QueryParams {

  /**
   * name=NAME
   * <p>
   * Specifies the user name (only one user is requestable at a time).
   */
  @NotNull
  private String name;

  /**
   * buddies=1
   * <p>
   * Turns on optional buddies reporting. Results are paged; see page parameter.
   */
  @Min(1)
  @Max(1)
  private Integer buddies;

  /**
   * guilds=1
   * <p>
   * Turns on optional guilds reporting. Results are paged; see page parameter.
   */
  @Min(1)
  @Max(1)
  private Integer guilds;

  /**
   * hot=1
   * <p>
   * Include the user's hot 10 list from their profile. Omitted if empty.
   */
  @Min(1)
  @Max(1)
  private Integer hot;

  /**
   * top=1
   * <p>
   * Include the user's top 10 list from their profile. Omitted if empty.
   */
  @Min(1)
  @Max(1)
  private Integer top;

  /**
   * domain=DOMAIN
   * <p>
   * Controls the domain for the users hot 10 and top 10 lists. The DOMAIN default is boardgame; valid values are:
   * <ul>
   *   <li>boardgame
   *   <li>rpg
   *   <li>videogame
   * </ul>
   */
  @Pattern(regexp = "^(boardgame|rpg|videogame)$")
  private String domain;

  /**
   * page=NNN
   * <p>
   * Specifies the page of buddy and guild results to return. The default page is 1 if you don't specify it; page size
   * is 100 records (Current implementation seems to return 1000 records). The page parameter controls paging for both
   * buddies and guilds list if both are specified. If a &lt;buddies&gt; or &lt;guilds&gt; node is empty, it means that you have
   * requested a page higher than that needed to list all the buddies/guilds or, if you're on page 1, it means that that
   * user has no buddies and is not part of any guilds.
   */
  @Min(1)
  private Integer page;

}
