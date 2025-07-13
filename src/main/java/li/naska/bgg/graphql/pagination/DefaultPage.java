package li.naska.bgg.graphql.pagination;

import java.util.List;

public record DefaultPage<T>(List<PageItem<T>> pageItems, PageInfo pageInfo, int totalItems)
    implements Page<T> {}
