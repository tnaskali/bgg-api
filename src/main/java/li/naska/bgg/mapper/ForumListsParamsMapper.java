package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggForumlistV2QueryParams;
import li.naska.bgg.resource.vN.model.ForumsParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ForumListsParamsMapper {

  BggForumlistV2QueryParams toBggModel(ForumsParams source);

}
