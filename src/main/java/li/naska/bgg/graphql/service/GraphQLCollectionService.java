package li.naska.bgg.graphql.service;

import com.boardgamegeek.collection.v2.Items;
import java.util.Optional;
import li.naska.bgg.graphql.model.enums.CollectionStatus;
import li.naska.bgg.graphql.model.enums.CollectionSubtype;
import li.naska.bgg.repository.BggCollectionV2Repository;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GraphQLCollectionService {

  private final BggCollectionV2Repository collectionV2Repository;

  public GraphQLCollectionService(BggCollectionV2Repository collectionV2Repository) {
    this.collectionV2Repository = collectionV2Repository;
  }

  public Mono<Items> getItems(String username) {
    return getItems(username, null, CollectionSubtype.BOARDGAME);
  }

  public Mono<Items> getItems(String username, CollectionSubtype subtype) {
    return getItems(username, null, subtype);
  }

  public Mono<Items> getItems(String username, CollectionStatus status, CollectionSubtype subtype) {
    BggCollectionV2QueryParams params = new BggCollectionV2QueryParams();
    params.setUsername(username);
    if (status != null) {
      switch (status) {
        case OWN -> params.setOwn(1);
        case PREV_OWNED -> params.setPrevowned(1);
        case FOR_TRADE -> params.setTrade(1);
        case WANT_IN_TRADE -> params.setWant(1);
        case WANT_TO_PLAY -> params.setWanttoplay(1);
        case WANT_TO_BUY -> params.setWanttobuy(1);
        case WISHLIST -> params.setWishlist(1);
        case PRE_ORDERED -> params.setPreordered(1);
      }
    }
    switch (subtype) {
      case BOARDGAME -> params.setSubtype("boardgame");
      case BOARDGAME_EXPANSION -> params.setSubtype("boardgameexpansion");
      case BOARDGAME_ACCESSORY -> params.setSubtype("boardgameaccessory");
      case BOARDGAME_ISSUE -> params.setSubtype("boardgameissue");
      case RPG_ITEM -> params.setSubtype("rpgitem");
      case RPG_ISSUE -> params.setSubtype("rpgissue");
      case VIDEOGAME -> params.setSubtype("videogame");
      case VIDEOGAME_COMPILATION -> params.setSubtype("videogamecompilation");
      case VIDEOGAME_EXPANSION -> params.setSubtype("videogameexpansion");
      case VIDEOGAME_HARDWARE -> params.setSubtype("videogamehardware");
    }
    return collectionV2Repository.getItems(Optional.empty(), params);
  }
}
