package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggUserQueryParams;
import li.naska.bgg.resource.v3.model.UserParams;
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
public interface UserParamsMapper {

  @Mapping(target = "name", source = "username")
  BggUserQueryParams toBggModel(UserParams source);

}
