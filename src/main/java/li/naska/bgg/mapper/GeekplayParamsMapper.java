package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGeekplayRequestBody;
import li.naska.bgg.resource.vN.model.Play;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GeekplayParamsMapper {

  @Mapping(target = "ajax", ignore = true)
  @Mapping(target = "action", ignore = true)
  @Mapping(target = "finalize", ignore = true)
  @Mapping(target = "twitter", ignore = true)
  @Mapping(target = "twitter_username", ignore = true)
  @Mapping(target = "twitter_password", ignore = true)
  @Mapping(target = "playid", source = "id")
  @Mapping(target = "playdate", source = "date")
  @Mapping(target = "objecttype", source = "item.objecttype")
  @Mapping(target = "objectid", source = "item.objectid")
  BggGeekplayRequestBody toBggModel(Play source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"userid"})
  BggGeekplayRequestBody.GeekplayPlayer toBggModel(Play.Player source);

}
