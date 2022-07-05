package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGeeklistV1QueryParams;
import li.naska.bgg.resource.vN.model.GeeklistParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class
    }
)
public interface GeeklistParamsMapper {

  @Mapping(target = "start", ignore = true)
  @Mapping(target = "count", ignore = true)
  BggGeeklistV1QueryParams toBggModel(GeeklistParams source);

}
