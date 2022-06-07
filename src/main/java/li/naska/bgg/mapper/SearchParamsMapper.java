package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggSearchV2QueryParams;
import li.naska.bgg.resource.vN.model.SearchParams;
import org.mapstruct.Mapper;
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
public interface SearchParamsMapper {

  BggSearchV2QueryParams toBggModel(SearchParams source);

}
