package li.naska.bgg.graphql.pagination;

import java.util.List;

public interface Page<T> {

  List<PageItem<T>> pageItems();

  PageInfo pageInfo();

  int totalItems();
}
