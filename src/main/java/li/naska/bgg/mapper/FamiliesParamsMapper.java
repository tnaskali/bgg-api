package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggFamiliesQueryParams;
import li.naska.bgg.resource.v3.model.FamiliesParams;
import org.mapstruct.Mapper;
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
public interface FamiliesParamsMapper {

  BggFamiliesQueryParams toBggModel(FamiliesParams source);

}
