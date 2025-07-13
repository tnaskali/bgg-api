package li.naska.bgg.graphql.pagination;

import static graphql.Assert.assertNotNull;
import static java.lang.String.format;

import graphql.TrivialDataFetcher;
import graphql.collect.ImmutableKit;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ListPageDataFetcher<T> implements TrivialDataFetcher<Page<T>> {

  private final List<T> items;

  public ListPageDataFetcher(List<T> items) {
    this.items = assertNotNull(items, () -> "items cannot be null");
  }

  private List<PageItem<T>> buildPageItems() {
    return IntStream.range(0, items.size())
        .mapToObj(i -> new DefaultPageItem<>(items.get(i), i))
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public Page<T> get(DataFetchingEnvironment environment) {

    List<PageItem<T>> pageItems = buildPageItems();

    if (pageItems.isEmpty()) {
      return emptyPage();
    }

    int firstPresliceCursor = pageItems.get(0).index();
    int lastPresliceCursor = pageItems.get(pageItems.size() - 1).index();

    int afterOffset = getOffsetFromCursor(environment.getArgument("after"), -1);
    int begin = Math.max(afterOffset, -1) + 1;
    int beforeOffset = getOffsetFromCursor(environment.getArgument("before"), pageItems.size());
    int end = Math.min(beforeOffset, pageItems.size());

    if (begin > end) {
      begin = end;
    }

    pageItems = pageItems.subList(begin, end);
    if (pageItems.isEmpty()) {
      return emptyPage();
    }

    Integer first = environment.getArgument("first");
    Integer last = environment.getArgument("last");

    if (first != null) {
      if (first < 0) {
        throw new IllegalStateException(
            format("The page size must not be negative: 'first'=%s", first));
      }
      pageItems = pageItems.subList(0, first <= pageItems.size() ? first : pageItems.size());
    }
    if (last != null) {
      if (last < 0) {
        throw new IllegalStateException(
            format("The page size must not be negative: 'last'=%s", last));
      }
      pageItems = pageItems.subList(
          last > pageItems.size() ? 0 : pageItems.size() - last, pageItems.size());
    }

    if (pageItems.isEmpty()) {
      return emptyPage();
    }

    PageItem<T> firstEdge = pageItems.get(0);
    PageItem<T> lastEdge = pageItems.get(pageItems.size() - 1);

    PageInfo pageInfo = new DefaultPageInfo(
        firstEdge.index(),
        lastEdge.index(),
        firstEdge.index() != firstPresliceCursor,
        lastEdge.index() != lastPresliceCursor);

    return new DefaultPage<>(pageItems, pageInfo, items.size());
  }

  private Page<T> emptyPage() {
    PageInfo pageInfo = new DefaultPageInfo(0, 0, false, false);
    return new DefaultPage<>(ImmutableKit.emptyList(), pageInfo, 0);
  }

  private int getOffsetFromCursor(Integer index, int defaultValue) {
    return Optional.ofNullable(index).orElse(defaultValue);
  }
}
