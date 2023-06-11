package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggCollectionV1QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description = """
          In (or exclude for 0) a user's collection. That is, the user currently owns it. Collections include games that
          <p>
          <li>Syntax: /collection/{username}?own={0,1}
          <li>Example: /collection/zefquaavius?own=0 - games in zefquaavius' collection that he doesn't own
          <li>Example: /collection/zefquaavius?own=1 - games zefquaavius owns
          """
  )
  private Integer own;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?rated={0,1}
          <li>Example: /collection/zefquaavius?rated=0 - games in zefquaavius' collection that he has not rated
          <li>Example: /collection/zefquaavius?rated=1 - games zefquaavius has rated
          """
  )
  private Integer rated;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?played={0,1}
          <li>Example: /collection/zefquaavius?played=0 - games in zefquaavius' collection for which he has never logged any plays
          <li>Example: /collection/zefquaavius?played=1 - games for which zefquaavius has logged plays
          """
  )
  private Integer played;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          include (or exclude) games with comments
          <p>
          <li>Syntax: /collection/{username}?comment={0,1}
          <li>Example: /collection/zefquaavius?comment=0 - games in zefquaavius' collection upon which he currently has comments
          <li>Example: /collection/zefquaavius?comment=1 - games upon which zefquaavius has comments
          """
  )
  private Integer comment;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          games listed for trade
          <p>
          <li>Syntax: /collection/{username}?trade={0,1}
          <li>Example: /collection/zefquaavius?trade=0 - games in zefquaavius' collection he is not offering for trade
          <li>Example: /collection/zefquaavius?trade=1 - games zefquaavius is offering for trade
          """
  )
  private Integer trade;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?want={0,1}
          <li>Example: /collection/zefquaavius?want=0 - games in zefquaavius' collection of which he does not want (i.e. He's not looking to add it his actual collection.)
          <li>Example: /collection/zefquaavius?want=1 - games for which zefquaavius wants to acquire
          """
  )
  private Integer want;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?wantintrade={0,1}
          <li>Example: /collection/zefquaavius?wantintrade=0 - games in zefquaavius' collection of which he does not want (i.e. He's not looking to add it his actual collection.)
          <li>Example: /collection/zefquaavius?wantintrade=1 - games for which zefquaavius wants to acquire
          """
  )
  private Integer wantintrade;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?wishlist={0,1}
          <li>Example: /collection/zefquaavius?wishlist=0 - games in zefquaavius' collection that are not on his wishlist
          <li>Example: /collection/zefquaavius?wishlist=1 - games in zefquaavius' wishlist
          """
  )
  private Integer wishlist;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?wanttoplay={0,1}
          <li>Example: /collection/zefquaavius?wanttoplay=0 - games in zefquaavius' collection which he has no pressing desire to play
          <li>Example: /collection/zefquaavius?wanttoplay=1 - games zefquaavius currently wants to play
          """
  )
  private Integer wanttoplay;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?wanttobuy={0,1}
          <li>Example: /collection/zefquaavius?wanttobuy=0 - games in zefquaavius' collection he doesn't wish to buy
          <li>Example: /collection/zefquaavius?wanttobuy=1 - games zefquaavius wishes to buy
          """
  )
  private Integer wanttobuy;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?prevowned={0,1}
          <li>Example: /collection/zefquaavius?prevowned=0 - games in zefquaavius' collection he has not previously owned
          <li>Example: /collection/zefquaavius?prevowned=1 - games zefquaavius previously owned
          """
  )
  private Integer prevowned;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?preordered={0,1}
          <li>Example: /collection/zefquaavius?preordered=0 - games in zefquaavius' collection he has not pre-ordered
          <li>Example: /collection/zefquaavius?preordered=1 - games zefquaavius has pre-ordered
          """
  )
  private Integer preordered;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?hasparts={0,1}
          <li>Example: /collection/zefquaavius?hasparts=0 - games in zefquaavius' collection he has no spare parts of up for offer
          <li>Example: /collection/zefquaavius?hasparts=1 - zefquaavius has spare parts of these games up for offer
          """
  )
  private Integer hasparts;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?wantparts={0,1}
          <li>Example: /collection/zefquaavius?wantparts=0 - games in zefquaavius' collection he is not looking for spare parts of
          <li>Example: /collection/zefquaavius?wantparts=1 - zefquaavius is looking for spare parts for these games
          """
  )
  private Integer wantparts;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?notifycontent={0,1}
          <li>Example: /collection/zefquaavius?notifycontent=0 - zefquaavius does not get a geekmail whenever these games receive new content on BGG
          <li>Example: /collection/zefquaavius?notifycontent=1 - zefquaavius gets a geekmail whenever these games receive new content on BGG
          """
  )
  private Integer notifycontent;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?notifysale={0,1}
          <li>Example: /collection/zefquaavius?notifysale=0 - zefquaavius does not get a geekmail whenever a copy of these games are listed in BGGM
          <li>Example: /collection/zefquaavius?notifysale=1 - zefquaavius gets a geekmail whenever a copy of these games are listed in BGGM
          """
  )
  private Integer notifysale;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?notifyauction={0,1}
          <li>Example: /collection/zefquaavius?notifyauction=0 - zefquaavius does not get a geekmail whenever a copy is listed in a geekgold or eBay auction that's listed on BGG
          <li>Example: /collection/zefquaavius?notifyauction=1 - zefquaavius gets a geekmail whenever a copy is listed in a geekgold or eBay auction that's listed on BGG
          """
  )
  private Integer notifyauction;

  @Min(1)
  @Max(5)
  @Parameter(
      description = """
          <li>Syntax: /collection/{username}?wishlistpriority={1,5}
          <li>Example: /collection/zefquaavius?wishlistpriority=4 - games zefquaavius has given a wishlist priority of 4 ("Thinking about it")
          """
  )
  private Integer wishlistpriority;

  @Min(1)
  @Max(10)
  @Parameter(
      description = """
          minimum user rating
          <p>
          <li>Syntax: /collection/{username}?minrating={1,10}
          <li>Example: /collection/zefquaavius?minrating=9 - games zefquaavius has given a rating of at least 9
          """
  )
  private Integer minrating;

  @Min(1)
  @Max(10)
  @Parameter(
      description = """
          maximum user rating
          <p>
          <i>Note</i> : May be broken in BGG 2.0, or may require other parameters.
          <p>
          <li>Syntax: /collection/{username}?maxrating={1,10}
          <li>Example: /collection/zefquaavius?maxrating=2 - games zefquaavius has given a rating of no more than 2
          """
  )
  private Integer maxrating;

  @Min(1)
  @Max(10)
  @Parameter(
      description = """
          minimum bgg rating
          <p>
          <li>Syntax: /collection/{username}?minbggrating={1,10}
          <li>Example: /collection/zefquaavius?minbggrating=8 - games in zefquaavius' collection with an average BGG rating of at least 8
          """
  )
  private Integer minbggrating;

  @Min(1)
  @Max(10)
  @Parameter(
      description = """
          minimum bgg rating
          <p>
          <i>Note</i> : May be broken in BGG 2.0, or may require other parameters.
          <p>
          <li>Syntax</li> : /collection/{username}?maxbggrating={1,10}
          <p>
          <li>Example</li> : /collection/zefquaavius?maxbggrating=2 - games in zefquaavius' collection with an average BGG rating no more than 2
          """
  )
  private Integer maxbggrating;

  @Min(1)
  @Parameter(
      description = """
          minimum number of recorded plays
          <p>
          <li>Syntax: /collection/{username}?minplays={1,}
          <li>Example: /collection/zefquaavius?minplays=50 - games zefquaavius has played 50 or more times
          <li>Example: /collection/zefquaavius?minplays=50&maxplays=99 - games zefquaavius has played more than 49 times, but fewer than 100 times
          """
  )
  private Integer minplays;

  @Min(1)
  @Parameter(
      description = """
          maximum number of recorded plays
          <p>
          <li>Syntax: /collection/{username}?maxplays={1,}
          <li>Example: /collection/zefquaavius?maxplays=99 - games zefquaavius has played fewer than 100 times
          <li>Example: /collection/zefquaavius?minplays=50&maxplays=99 - games zefquaavius has played more than 49 times, but fewer than 100 times
          """
  )
  private Integer maxplays;

  @Min(1)
  @Parameter(
      example = "1",
      description = """
          Show the private fields (purchase price, etc). Default is 1 for logged in users, 0 otherwise, and showprivate=1 only works on your own collection, when you are logged in.
          <p>
          <li>Syntax: /collection/{username}?showprivate={0,1}
          <li>Example: /collection/zefquaavius?showprivate=1 - include private info zefquaavius has listed, provided you're zefquaavius
          <li>Example: /collection/zefquaavius?showprivate=0 - If you're zefquaavius, see what everyone else sees when viewing your collection: No private info
          """
  )
  private Integer showprivate;

}
