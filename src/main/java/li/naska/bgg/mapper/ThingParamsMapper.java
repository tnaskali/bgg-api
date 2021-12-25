package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.resource.v3.model.ThingParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class,
        ListToCommaSeparatedStringMapper.class
    }
)
public interface ThingParamsMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "historical", ignore = true)
  @Mapping(target = "from", ignore = true)
  @Mapping(target = "to", ignore = true)
  BggThingsQueryParams toBggModel(ThingParams source);

}
