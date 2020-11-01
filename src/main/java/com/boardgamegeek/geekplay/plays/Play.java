package com.boardgamegeek.geekplay.plays;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import li.naska.bgg.model.common.BggObjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Play {
    @JsonProperty
    private Integer playid;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate playdate;
    @JsonProperty
    private String comments;
    @JsonProperty
    private Integer length;
    @JsonProperty
    private String location;
    @JsonProperty(required = true)
    private BggObjectType objecttype;
    @JsonProperty
    private Integer objectid;
    @JsonProperty
    private Integer quantity;
    @JsonProperty
    private List<Player> players;
    @JsonProperty(required = true)
    private boolean twitter;
    @JsonProperty(required = true)
    private boolean incomplete;
    @JsonProperty(required = true)
    private boolean nowinstats;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Player {
        @JsonProperty(required = true)
        private String username;
        @JsonProperty(required = true)
        private String name;
        @JsonProperty
        private String position;
        @JsonProperty
        private String color;
        @JsonProperty
        private String score;
        @JsonProperty
        private String rating;
        @JsonProperty(required = true)
        private boolean win;
        @JsonProperty(value = "new", required = true)
        private boolean _new;
    }
}
