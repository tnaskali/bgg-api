package li.naska.bgg.mapper;

import li.naska.bgg.resource.vN.model.Forum;
import li.naska.bgg.resource.vN.model.Thread;
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
  @Mapping(target = "groupid", ignore = true)
  @Mapping(target = "description", ignore = true)
  Forum fromBggModel(com.boardgamegeek.forum.v2.Forum source);

  @Mapping(target = "threads", ignore = true)
  Forum fromBggModel(com.boardgamegeek.forumlist.v2.Forum source);

  @Mapping(target = "link", ignore = true)
  @Mapping(target = "articles", ignore = true)
  Thread fromBggModel(com.boardgamegeek.forum.v2.Thread source);

  default List<Thread> getThreads(com.boardgamegeek.forum.v2.Forum source) {
    return Optional.ofNullable(source.getThreads())
        .map(com.boardgamegeek.forum.v2.Threads::getThreads)
        .map(l -> l.stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
