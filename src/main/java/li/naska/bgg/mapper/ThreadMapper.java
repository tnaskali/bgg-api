package li.naska.bgg.mapper;

import com.boardgamegeek.thread.Articles;
import li.naska.bgg.resource.v3.model.Thread;
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
  Thread fromBggModel(com.boardgamegeek.thread.Thread source);

  Thread.Article fromBggModel(com.boardgamegeek.thread.Article source);

  default List<Thread.Article> getArticles(com.boardgamegeek.thread.Thread source) {
    return Optional.ofNullable(source.getArticles())
        .map(Articles::getArticle)
        .map(l -> l.stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

}
