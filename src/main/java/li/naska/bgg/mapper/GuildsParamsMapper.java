package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggGuildQueryParams;
import li.naska.bgg.resource.v3.model.GuildParams;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class
    }
)
public interface GuildsParamsMapper {

  BggGuildQueryParams toBggModel(GuildParams source);

}
