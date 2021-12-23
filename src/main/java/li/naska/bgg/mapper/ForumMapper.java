package li.naska.bgg.mapper;

import com.boardgamegeek.forum.Threads;
import li.naska.bgg.resource.v3.model.Forum;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ForumMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"termsofuse", "threads"})
  @Mapping(target = "threads", expression = "java(getThreads(source))")
  Forum fromBggModel(com.boardgamegeek.forum.Forum source);

  Forum.ForumThread fromBggModel(com.boardgamegeek.forum.Thread source);

  default List<Forum.ForumThread> getThreads(com.boardgamegeek.forum.Forum source) {
    return Optional.ofNullable(source.getThreads())
        .map(Threads::getThread)
        .map(l -> l.stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
