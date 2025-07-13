package li.naska.bgg.graphql.pagination;

public record DefaultPageItem<T>(T item, int index) implements PageItem<T> {}
