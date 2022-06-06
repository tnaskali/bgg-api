package li.naska.bgg.mapper;

import li.naska.bgg.resource.vN.model.Results;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ResultsMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = "termsofuse")
  Results fromBggModel(com.boardgamegeek.search.Results source);

  Results.Result fromBggModel(com.boardgamegeek.search.Result source);

}
