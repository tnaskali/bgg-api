package li.naska.bgg.graphql.model;

import lombok.Builder;

@Builder
public record CollectionItem(
    CollectionItemId id,
    String subtype,
    Integer collid,
    String name,
    String originalname,
    Integer yearpublished,
    Integer numplays) {

  public record CollectionItemId(Integer objectid, String objecttype) {}
}
