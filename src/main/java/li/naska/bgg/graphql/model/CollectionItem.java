package li.naska.bgg.graphql.model;

import lombok.Builder;

@Builder
public record CollectionItem(
    Integer objectid,
    String objecttype,
    String subtype,
    Integer collid,
    String name,
    String originalname,
    Integer yearpublished,
    Integer numplays) {}
