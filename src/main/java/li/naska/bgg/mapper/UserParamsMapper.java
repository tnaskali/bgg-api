package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggUserQueryParams;
import li.naska.bgg.resource.v3.model.UserParams;
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
public interface UserParamsMapper {

  @Mapping(target = "name", ignore = true)
  BggUserQueryParams toBggModel(UserParams source);

}
