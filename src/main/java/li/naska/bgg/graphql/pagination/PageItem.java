package li.naska.bgg.graphql.pagination;

public interface PageItem<T> {

  T item();

  int index();
}
