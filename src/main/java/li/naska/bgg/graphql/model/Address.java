package li.naska.bgg.graphql.model;

public record Address(
    String address1,
    String address2,
    String postalcode,
    String city,
    String stateorprovince,
    String isocountry,
    String country) {}
