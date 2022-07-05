package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
import li.naska.bgg.resource.vN.model.ItemPlaysParams;
import li.naska.bgg.resource.vN.model.UserPlaysParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface PlaysParamsMapper {

  @Mapping(target = "username", ignore = true)
  @Mapping(target = "page", ignore = true)
  BggPlaysV2QueryParams toBggModel(UserPlaysParams source);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "page", ignore = true)
  BggPlaysV2QueryParams toBggModel(ItemPlaysParams source);

}
