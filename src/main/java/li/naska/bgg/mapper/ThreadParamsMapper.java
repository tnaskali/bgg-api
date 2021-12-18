package li.naska.bgg.mapper;

import li.naska.bgg.repository.model.BggThreadQueryParams;
import li.naska.bgg.resource.v3.model.ThreadParams;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ThreadParamsMapper {

  static String getMinarticledate(ThreadParams s) {
    if (s.getMinarticledate() != null) {
      if (s.getMinarticletime() != null) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss").format(s.getMinarticledate().atTime(s.getMinarticletime()));
      } else {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(s.getMinarticledate());
      }
    } else {
      return null;
    }
  }

  @BeanMapping(ignoreUnmappedSourceProperties = {"minarticledate", "minarticletime"})
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "minarticledate", expression = "java(ThreadParamsMapper.getMinarticledate(source))")
  BggThreadQueryParams toBggModel(ThreadParams source);

}
