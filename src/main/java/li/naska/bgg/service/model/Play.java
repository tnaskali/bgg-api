package li.naska.bgg.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Play {
  @JsonProperty(value = "date", required = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate date;
  @JsonProperty(value = "comments")
  private String comments;
  @JsonProperty(value = "length")
  private Integer durationInMinutes;
  @JsonProperty(value = "location")
  private String location;
  @JsonProperty(value = "objecttype", required = true)
  private ObjectType objectType;
  @JsonProperty(value = "objectid")
  private Integer objectId;
  @JsonProperty(value = "quantity")
  private Integer numberOfPlays;
  @JsonProperty(value = "players")
  private List<BggPlayer> players;
  @JsonProperty(value = "twitter", required = true)
  private boolean publishOnTwitter;
  @JsonProperty(value = "incomplete", required = true)
  private boolean unfinished;
  @JsonProperty(value = "nowinstats", required = true)
  private boolean ignoreInStats;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BggPlayer {
    @JsonProperty(value = "username", required = true)
    private String bggUsername;
    @JsonProperty(value = "name", required = true)
    private String name;
    @JsonProperty(value = "position")
    private String startingPosition;
    @JsonProperty(value = "color")
    private String color;
    @JsonProperty(value = "score")
    private String score;
    @JsonProperty(value = "rating")
    private String rating;
    @JsonProperty(value = "win", required = true)
    private boolean won;
    @JsonProperty(value = "new", required = true)
    private boolean firstTimePlayer;
  }
}