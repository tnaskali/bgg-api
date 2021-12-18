package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggCollectionQueryParams;
import li.naska.bgg.resource.v3.model.CollectionParams;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        BooleanToOneOrZeroStringMapper.class,
        ListToCommaSeparatedStringMapper.class
    }
)
public interface CollectionParamsMapper {

  static String getModifiedsince(CollectionParams s) {
    if (s.getModifiedsincedate() != null) {
      if (s.getModifiedsincetime() != null) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss").format(s.getModifiedsincedate().atTime(s.getModifiedsincetime()));
      } else {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(s.getModifiedsincedate());
      }
    } else {
      return null;
    }
  }

  @BeanMapping(ignoreUnmappedSourceProperties = {"modifiedsincedate", "modifiedsincetime"})
  @Mapping(target = "excludesubtype", ignore = true)
  @Mapping(target = "rating", source = "maxrating")
  @Mapping(target = "bggrating", source = "maxbggrating")
  @Mapping(target = "modifiedsince", expression = "java(CollectionParamsMapper.getModifiedsince(source))")
  BggCollectionQueryParams toBggModel(CollectionParams source);

}
