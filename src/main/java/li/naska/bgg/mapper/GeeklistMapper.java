package li.naska.bgg.mapper;

import li.naska.bgg.resource.vN.model.Geeklist;
import org.mapstruct.*;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface GeeklistMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"termsofuse", "postdateTimestamp", "editdateTimestamp"})
  @Mapping(target = "postdate_timestamp", source = "postdateTimestamp")
  @Mapping(target = "editdate_timestamp", source = "editdateTimestamp")
  Geeklist fromBggModel(com.boardgamegeek.geeklist.v1.Geeklist source);

  Geeklist.Comment fromBggModel(com.boardgamegeek.geeklist.v1.Comment source);

  Geeklist.Item fromBggModel(com.boardgamegeek.geeklist.v1.Item source);

}
