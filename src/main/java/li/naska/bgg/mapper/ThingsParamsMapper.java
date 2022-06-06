package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.resource.vN.model.ThingsParams;
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
public interface ThingsParamsMapper {

  @Mapping(target = "id", source = "ids")
  @Mapping(target = "type", source = "types")
  @Mapping(target = "videos", ignore = true)
  @Mapping(target = "versions", ignore = true)
  @Mapping(target = "marketplace", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "ratingcomments", ignore = true)
  @Mapping(target = "page", ignore = true)
  @Mapping(target = "pagesize", ignore = true)
  @Mapping(target = "historical", ignore = true)
  @Mapping(target = "from", ignore = true)
  @Mapping(target = "to", ignore = true)
  BggThingsQueryParams toBggModel(ThingsParams source);

}
