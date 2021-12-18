package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGeeklistQueryParams;
import li.naska.bgg.resource.v3.model.GeeklistParams;
import org.mapstruct.BeanMapping;
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
public interface GeeklistParamsMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = "id")
  @Mapping(target = "start", ignore = true)
  @Mapping(target = "count", ignore = true)
  BggGeeklistQueryParams toBggModel(GeeklistParams source);

}
