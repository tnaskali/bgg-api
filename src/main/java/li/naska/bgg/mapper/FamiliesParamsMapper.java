package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggFamiliesQueryParams;
import li.naska.bgg.resource.vN.model.FamiliesParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        ListToCommaSeparatedStringMapper.class
    }
)
public interface FamiliesParamsMapper {

  BggFamiliesQueryParams toBggModel(FamiliesParams source);

}
