package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggForumsQueryParams;
import li.naska.bgg.resource.v3.model.ForumsParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ForumListsParamsMapper {

  @Mapping(target = "id", ignore = true)
  BggForumsQueryParams toBggModel(ForumsParams source);

}
