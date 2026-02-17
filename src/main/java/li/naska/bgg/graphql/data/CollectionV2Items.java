package li.naska.bgg.graphql.data;

import com.boardgamegeek.xml.collection.v2.Items;
import li.naska.bgg.graphql.model.enums.CollectionSubtype;

public record CollectionV2Items(Items items) {

  public record CollectionItemsKey(String username, CollectionSubtype subtype) {}
}
