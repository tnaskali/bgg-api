package li.naska.bgg.collection.resource;

import com.boardgamegeek.xmlapi2.hot.ItemSubtypeEnum;
import com.boardgamegeek.xmlapi2.hot.Items;
import li.naska.bgg.collection.service.BggHotItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hotitems")
public class BggHotItemsResource {

  @Autowired
  private BggHotItemsService bggHotItemsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Items getItems(@RequestParam("subtype") ItemSubtypeEnum itemSubtype) {
    return bggHotItemsService.getHotItems(itemSubtype);
  }

}
