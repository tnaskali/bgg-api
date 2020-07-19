package li.naska.bgg.service.model.plays;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import li.naska.bgg.service.model.common.ObjectType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Play {

  @JsonProperty(required = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate playdate;
  @JsonProperty
  private String comment;
  @JsonProperty
  private Integer length;
  @JsonProperty(required = true)
  private boolean twitter;
  private Integer minutes;
  private String location;
  private Integer objectid;
  private Integer hours;
  private Integer quantity;
  private List<Player> players;
  @JsonProperty(required = true)
  private ObjectType objecttype;

}
