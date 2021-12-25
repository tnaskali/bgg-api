package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGuildQueryParams;
import li.naska.bgg.resource.v3.model.GuildParams;
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
public interface GuildsParamsMapper {

  @Mapping(target = "id", ignore = true)
  BggGuildQueryParams toBggModel(GuildParams source);

}
