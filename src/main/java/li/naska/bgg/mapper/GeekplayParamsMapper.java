package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGeekplayRequestBody;
import li.naska.bgg.resource.v3.model.Play;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GeekplayParamsMapper {

  @Mapping(target = "ajax", ignore = true)
  @Mapping(target = "action", ignore = true)
  @Mapping(target = "finalize", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "twitter", ignore = true)
  @Mapping(target = "twitter_username", ignore = true)
  @Mapping(target = "twitter_password", ignore = true)
  @Mapping(target = "playid", source = "id")
  @Mapping(target = "playdate", source = "date")
  @Mapping(target = "length", source = "durationInMinutes")
  @Mapping(target = "objecttype", source = "objectType")
  @Mapping(target = "objectid", source = "objectId")
  @Mapping(target = "quantity", source = "numberOfPlays")
  @Mapping(target = "incomplete", source = "unfinished")
  @Mapping(target = "nowinstats", source = "noWinStats")
  BggGeekplayRequestBody toBggModel(Play source);

  @Mapping(target = "username", source = "bggUsername")
  @Mapping(target = "position", source = "startingPosition")
  @Mapping(target = "win", source = "won")
  @Mapping(target = "_new", source = "firstTimePlayer")
  BggGeekplayRequestBody.GeekplayPlayer toBggModel(Play.Player source);

}
