package li.naska.bgg.graphql.pagination;

public record DefaultPageInfo(
    int startIndex, int endIndex, boolean hasPreviousPage, boolean hasNextPage)
    implements PageInfo {}
