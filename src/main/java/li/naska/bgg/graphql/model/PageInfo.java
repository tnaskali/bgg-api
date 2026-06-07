package li.naska.bgg.graphql.model;

public record PageInfo(
    boolean hasPreviousPage, boolean hasNextPage, String startCursor, String endCursor) {}
