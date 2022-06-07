package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGuildV2QueryParams;
import li.naska.bgg.resource.vN.model.GuildMembersParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class
    }
)
public interface GuildMembersParamsMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "page", ignore = true)
  @Mapping(target = "members", ignore = true)
  BggGuildV2QueryParams toBggModel(GuildMembersParams source);

}
