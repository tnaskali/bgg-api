package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggPlaysQueryParams;
import li.naska.bgg.resource.v3.model.PlaysParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface PlaysParamsMapper {

  BggPlaysQueryParams toBggModel(PlaysParams source);

}
