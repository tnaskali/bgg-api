package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggHotV2QueryParams;
import li.naska.bgg.resource.vN.model.HotItemsParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface HotItemsParamsMapper {

  BggHotV2QueryParams toBggModel(HotItemsParams source);

}
