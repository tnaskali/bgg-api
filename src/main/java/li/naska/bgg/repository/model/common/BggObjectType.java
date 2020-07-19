package li.naska.bgg.repository.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BggObjectType {

  THING("thing"), FAMILY("family");

  private final String value;

  BggObjectType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

}
