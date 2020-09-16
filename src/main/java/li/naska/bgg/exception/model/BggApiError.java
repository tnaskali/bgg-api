package li.naska.bgg.exception.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class BggApiError {

  @JsonProperty
  private ZonedDateTime timestamp;
  @JsonProperty
  private int status;
  @JsonProperty
  private String error;
  @JsonProperty
  private String message;
  @JsonProperty
  private String path;

}
