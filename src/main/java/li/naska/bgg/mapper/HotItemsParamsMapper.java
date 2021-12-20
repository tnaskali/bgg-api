package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggHotItemsQueryParams;
import li.naska.bgg.resource.v3.model.HotItemsParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface HotItemsParamsMapper {

  BggHotItemsQueryParams toBggModel(HotItemsParams source);

}
