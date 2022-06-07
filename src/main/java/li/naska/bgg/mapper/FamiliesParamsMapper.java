package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggFamilyV2QueryParams;
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

  BggFamilyV2QueryParams toBggModel(FamiliesParams source);

}
