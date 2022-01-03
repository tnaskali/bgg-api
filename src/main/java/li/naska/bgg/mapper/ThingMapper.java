package li.naska.bgg.mapper;

import com.boardgamegeek.enums.NameType;
import com.boardgamegeek.thing.Comments;
import com.boardgamegeek.thing.Result;
import li.naska.bgg.resource.v3.model.Name;
import li.naska.bgg.resource.v3.model.Thing;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        StringValueToStringMapper.class,
        IntegerValueToIntegerMapper.class,
        DecimalValueToBigDecimalMapper.class,
        ZonedDateTimeValueToZonedDateTimeMapper.class
    }
)
public interface ThingMapper extends BaseMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {
      "names", "thumbnails", "images", "descriptions",
      "yearpublisheds", "datepublisheds", "issueindices", "releasedates",
      "seriescodes", "polls", "minplayers", "maxplayers",
      "playingtimes", "minplaytimes", "maxplaytimes", "minages",
      "comments", "statistics", "videos", "marketplacelistings",
      "versions"
  })
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternatenames", expression = "java(getAlternatenames(source))")
  @Mapping(target = "thumbnail", expression = "java(getFirstValue(source.getThumbnails()))")
  @Mapping(target = "image", expression = "java(getFirstValue(source.getImages()))")
  @Mapping(target = "description", expression = "java(getFirstValue(source.getDescriptions()))")
  @Mapping(target = "yearpublished", expression = "java(getYearpublished(source))")
  @Mapping(target = "datepublished", expression = "java(getDatepublished(source))")
  @Mapping(target = "releasedate", expression = "java(getFirstLocalDateValue(source.getReleasedates()))")
  @Mapping(target = "seriescode", expression = "java(getFirstStringValue(source.getSeriescodes()))")
  @Mapping(target = "issueindex", expression = "java(getFirstIntegerValue(source.getIssueindices()))")
  @Mapping(target = "minplayers", expression = "java(getFirstIntegerValue(source.getMinplayers()))")
  @Mapping(target = "maxplayers", expression = "java(getFirstIntegerValue(source.getMaxplayers()))")
  @Mapping(target = "playingtime", expression = "java(getFirstIntegerValue(source.getPlayingtimes()))")
  @Mapping(target = "minplaytime", expression = "java(getFirstIntegerValue(source.getMinplaytimes()))")
  @Mapping(target = "maxplaytime", expression = "java(getFirstIntegerValue(source.getMaxplaytimes()))")
  @Mapping(target = "minage", expression = "java(getFirstIntegerValue(source.getMinages()))")
  @Mapping(target = "videos", expression = "java(getVideos(source))")
  @Mapping(target = "numcomments", expression = "java(getNumcomments(source))")
  @Mapping(target = "comments", expression = "java(getComments(source))")
  @Mapping(target = "versions", expression = "java(getVersions(source))")
  @Mapping(target = "statistics", expression = "java(getStatistics(source))")
  @Mapping(target = "marketplacelistings", expression = "java(getMarketplaceListings(source))")
  Thing fromBggModel(com.boardgamegeek.thing.Thing source);

  default Thing.Statistics getStatistics(com.boardgamegeek.thing.Thing source) {
    return source.getStatistics().stream()
        .flatMap(e -> e.getRatings().stream())
        .map(this::fromBggModel)
        .findFirst()
        .orElse(null);
  }

  Thing.Link fromBggModel(com.boardgamegeek.thing.Link source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"results"})
  @Mapping(target = "numvotes", source = "totalvotes")
  @Mapping(target = "results", expression = "java(getResults(source))")
  Thing.Poll fromBggModel(com.boardgamegeek.thing.Poll source);

  @Mapping(target = "votes", source = "numvotes")
  @Mapping(target = "votesforbest", ignore = true)
  @Mapping(target = "votesforrecommended", ignore = true)
  @Mapping(target = "votesfornotrecommended", ignore = true)
  Thing.Poll.Result fromBggModel(com.boardgamegeek.thing.Result source);

  default List<Thing.Poll.Result> getResults(com.boardgamegeek.thing.Poll source) {
    if ("suggested_numplayers".equals(source.getName())) {
      return source.getResults().stream()
          .map(r -> {
            Function<String, Integer> votesFunction = (name) -> r.getResult().stream()
                .filter(e -> name.equals(e.getValue()))
                .map(Result::getNumvotes)
                .findFirst()
                .orElse(null);
            Thing.Poll.Result result = new Thing.Poll.Result();
            result.setValue(r.getNumplayers());
            result.setVotesforbest(votesFunction.apply("Best"));
            result.setVotesforrecommended(votesFunction.apply("Recommended"));
            result.setVotesfornotrecommended(votesFunction.apply("Not Recommended"));
            return result;
          })
          .collect(Collectors.toList());
    } else {
      return source.getResults().stream()
          .flatMap(e -> e.getResult().stream())
          .map(this::fromBggModel)
          .collect(Collectors.toList());
    }
  }

  @BeanMapping(ignoreUnmappedSourceProperties = {
      "thumbnails", "images", "names", "links", "yearpublisheds",
      "productcodes", "widths", "lengths", "depths", "weights"
  })
  @Mapping(target = "thumbnail", expression = "java(getFirstValue(source.getThumbnails()))")
  @Mapping(target = "image", expression = "java(getFirstValue(source.getImages()))")
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternatenames", expression = "java(getAlternatenames(source))")
  @Mapping(target = "yearpublished", expression = "java(getFirstIntegerValue(source.getYearpublisheds()))")
  @Mapping(target = "productcode", expression = "java(getFirstStringValue(source.getProductcodes()))")
  @Mapping(target = "width", expression = "java(getFirstBigDecimalValue(source.getWidths()))")
  @Mapping(target = "length", expression = "java(getFirstBigDecimalValue(source.getLengths()))")
  @Mapping(target = "depth", expression = "java(getFirstBigDecimalValue(source.getDepths()))")
  @Mapping(target = "weight", expression = "java(getFirstBigDecimalValue(source.getWeights()))")
  Thing.Version fromBggModel(com.boardgamegeek.thing.Version source);

  Thing.Version.VersionLink fromBggModel(com.boardgamegeek.thing.VersionLink source);

  Thing.Video fromBggModel(com.boardgamegeek.thing.Video source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"rating"})
  @Mapping(target = "rating", expression = "java(getRating(source))")
  Thing.Comment fromBggModel(com.boardgamegeek.thing.Comment source);

  default BigDecimal getRating(com.boardgamegeek.thing.Comment source) {
    return Optional.ofNullable(source.getRating())
        .map(o -> "N/A".equals(o) ? null : new BigDecimal(o))
        .orElse(null);
  }

  @BeanMapping(ignoreUnmappedSourceProperties = {"ranks", "date"})
  @Mapping(target = "ranks", expression = "java(getRanks(source))")
  Thing.Statistics fromBggModel(com.boardgamegeek.thing.Ratings source);

  default List<Thing.Statistics.Rank> getRanks(com.boardgamegeek.thing.Ratings source) {
    return Optional.ofNullable(source.getRanks())
        .map(o -> o.getRank().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

  Thing.Statistics.Rank fromBggModel(com.boardgamegeek.thing.Rank source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"price", "link"})
  @Mapping(target = "currency", expression = "java(source.getPrice().getCurrency())")
  @Mapping(target = "price", expression = "java(source.getPrice().getValue())")
  @Mapping(target = "link", expression = "java(source.getLink().getHref())")
  @Mapping(target = "title", expression = "java(source.getLink().getTitle())")
  Thing.MarketplaceListing fromBggModel(com.boardgamegeek.thing.Listing source);

  default Integer getYearpublished(com.boardgamegeek.thing.Thing source) {
    if (!source.getYearpublisheds().isEmpty()) {
      return source.getYearpublisheds().iterator().next().getValue();
    } else if (!source.getDatepublisheds().isEmpty()) {
      String value = source.getDatepublisheds().iterator().next().getValue();
      try {
        return LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(value)).getYear();
      } catch (DateTimeException e) {
        // example: 2008-00-00
        return value.length() >= 4 ? Integer.parseInt(value.substring(0, 4)) : null;
      }
    }
    return null;
  }

  default LocalDate getDatepublished(com.boardgamegeek.thing.Thing source) {
    if (!source.getDatepublisheds().isEmpty()) {
      try {
        return LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(source.getDatepublisheds().iterator().next().getValue()));
      } catch (DateTimeException e) {
        return null;
      }
    }
    return null;
  }

  default Name getName(com.boardgamegeek.thing.Thing source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default Name getName(com.boardgamegeek.thing.Version source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default List<Name> getAlternatenames(com.boardgamegeek.thing.Thing source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

  default List<Name> getAlternatenames(com.boardgamegeek.thing.Version source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

  default List<Thing.Video> getVideos(com.boardgamegeek.thing.Thing source) {
    return source.getVideos().stream()
        .flatMap(e -> e.getVideo().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.Version> getVersions(com.boardgamegeek.thing.Thing source) {
    return source.getVersions().stream()
        .flatMap(e -> e.getItem().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default Integer getNumcomments(com.boardgamegeek.thing.Thing source) {
    return source.getComments().stream()
        .findFirst()
        .map(Comments::getTotalitems)
        .orElse(null);
  }

  default List<Thing.Comment> getComments(com.boardgamegeek.thing.Thing source) {
    return source.getComments().stream()
        .flatMap(e -> e.getComment().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.MarketplaceListing> getMarketplaceListings(com.boardgamegeek.thing.Thing source) {
    return source.getMarketplacelistings().stream()
        .flatMap(e -> e.getListing().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

}
