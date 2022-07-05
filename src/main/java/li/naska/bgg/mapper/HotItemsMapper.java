package li.naska.bgg.mapper;

import li.naska.bgg.resource.vN.model.HotItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        StringValueToStringMapper.class,
        IntegerValueToIntegerMapper.class
    }
)
public interface HotItemsMapper {

  HotItem fromBggModel(com.boardgamegeek.hot.HotItem source);

}
