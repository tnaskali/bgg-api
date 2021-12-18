package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggForumsQueryParams;
import li.naska.bgg.resource.v3.model.ForumsParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ForumListsParamsMapper {

  BggForumsQueryParams toBggModel(ForumsParams source);

}
