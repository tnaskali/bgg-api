package li.naska.bgg.graphql.model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public record CollectionItemConnection(
    List<CollectionItemEdge> edges, PageInfo pageInfo, int totalCount) {

  public static CollectionItemConnection of(
      List<CollectionItem> items, Integer first, String after) {
    int totalCount = items.size();
    if (totalCount == 0) {
      return new CollectionItemConnection(List.of(), new PageInfo(false, false, null, null), 0);
    }

    int afterIndex = decodeCursor(after);
    int begin = Math.max(afterIndex, -1) + 1;
    int end = totalCount;

    if (begin > end) {
      begin = end;
    }

    List<CollectionItem> pageItems = items.subList(begin, end);

    if (first != null) {
      if (first < 0) {
        throw new IllegalArgumentException("The page size must not be negative: 'first'=" + first);
      }
      pageItems = pageItems.subList(0, Math.min(first, pageItems.size()));
    }

    if (pageItems.isEmpty()) {
      return new CollectionItemConnection(
          List.of(), new PageInfo(false, false, null, null), totalCount);
    }

    int sliceStart = begin;
    List<CollectionItemEdge> edges = new ArrayList<>();
    for (int i = 0; i < pageItems.size(); i++) {
      edges.add(new CollectionItemEdge(encodeCursor(sliceStart + i), pageItems.get(i)));
    }
    edges = List.copyOf(edges);

    boolean hasPreviousPage = sliceStart > 0;
    boolean hasNextPage = sliceStart + pageItems.size() < totalCount;
    String startCursor = edges.get(0).cursor();
    String endCursor = edges.get(edges.size() - 1).cursor();

    return new CollectionItemConnection(
        edges, new PageInfo(hasPreviousPage, hasNextPage, startCursor, endCursor), totalCount);
  }

  private static String encodeCursor(int index) {
    return Base64.getEncoder()
        .encodeToString(String.valueOf(index).getBytes(StandardCharsets.UTF_8));
  }

  private static int decodeCursor(String cursor) {
    if (cursor == null) {
      return -1;
    }
    return Integer.parseInt(new String(Base64.getDecoder().decode(cursor), StandardCharsets.UTF_8));
  }
}
