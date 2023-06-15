package li.naska.bgg.mapper;

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
public interface ThreadMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"termsofuse", "articles"})
  @Mapping(target = "articles", expression = "java(getArticles(source))")
  @Mapping(target = "author", ignore = true)
  @Mapping(target = "postdate", ignore = true)
  @Mapping(target = "lastpostdate", ignore = true)
  Thread fromBggModel(com.boardgamegeek.thread.v2.Thread source);

  Thread.Article fromBggModel(com.boardgamegeek.thread.v2.Article source);

  default List<Thread.Article> getArticles(com.boardgamegeek.thread.v2.Thread source) {
    return Optional.ofNullable(source.getArticles())
        .map(com.boardgamegeek.thread.v2.Articles::getArticles)
        .map(l -> l.stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
