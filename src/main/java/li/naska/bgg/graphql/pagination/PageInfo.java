package li.naska.bgg.graphql.pagination;

public interface PageInfo {

  int startIndex();

  int endIndex();

  boolean hasPreviousPage();

  boolean hasNextPage();
}
