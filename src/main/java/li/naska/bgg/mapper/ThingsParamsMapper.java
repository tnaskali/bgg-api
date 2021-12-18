package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.resource.v3.model.ThingsParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class,
        ListToCommaSeparatedStringMapper.class
    }
)
public interface ThingsParamsMapper {

  @Mapping(target = "historical", ignore = true)
  @Mapping(target = "from", ignore = true)
  @Mapping(target = "to", ignore = true)
  BggThingsQueryParams toBggModel(ThingsParams source);

}
