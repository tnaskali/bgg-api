package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggCollectionQueryParams;
import li.naska.bgg.resource.v3.model.CollectionParams;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class,
        ListToCommaSeparatedStringMapper.class
    }
)
public interface CollectionParamsMapper {

  default String getModifiedsince(CollectionParams source) {
    if (source.getModifiedsincedate() != null) {
      if (source.getModifiedsincetime() != null) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(source.getModifiedsincedate().atTime(source.getModifiedsincetime()));
      } else {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(source.getModifiedsincedate());
      }
    } else {
      return null;
    }
  }

  @BeanMapping(ignoreUnmappedSourceProperties = {"modifiedsincedate", "modifiedsincetime"})
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "excludesubtype", ignore = true)
  @Mapping(target = "id", source = "ids")
  @Mapping(target = "rating", source = "maxrating")
  @Mapping(target = "bggrating", source = "maxbggrating")
  @Mapping(target = "modifiedsince", expression = "java(getModifiedsince(source))")
  BggCollectionQueryParams toBggModel(CollectionParams source);

}
