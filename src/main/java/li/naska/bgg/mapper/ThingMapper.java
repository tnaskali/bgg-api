package li.naska.bgg.mapper;

import com.boardgamegeek.common.*;
import com.boardgamegeek.enums.NameType;
import com.boardgamegeek.thing.*;
import li.naska.bgg.resource.v3.model.Thing;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        StringValueToStringMapper.class,
        ZonedDateTimeValueToZonedDateTimeMapper.class
    }
)
public interface ThingMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {
      "names", "thumbnails", "images", "descriptions",
      "yearpublisheds", "datepublisheds", "issueindices", "releasedates",
      "seriescodes", "polls", "minplayers", "maxplayers",
      "playingtimes", "minplaytimes", "maxplaytimes", "minages",
      "comments", "statistics", "videos", "marketplacelistings",
      "versions"
  })
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternateNames", expression = "java(getAlternateNames(source))")
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
  @Mapping(target = "versions", expression = "java(getVersions(source))")
  @Mapping(target = "stats", expression = "java(getStats(source))")
  @Mapping(target = "marketplacelistings", expression = "java(getMarketplaceListings(source))")
  Thing fromBggModel(com.boardgamegeek.thing.Thing source);

  Thing.ThingLink fromBggModel(Link source);

  @BeanMapping(ignoreUnmappedSourceProperties = {
      "thumbnails", "images", "names", "links", "yearpublisheds",
      "productcodes", "widths", "lengths", "depths", "weights"
  })
  @Mapping(target = "thumbnail", expression = "java(getFirstValue(source.getThumbnails()))")
  @Mapping(target = "image", expression = "java(getFirstValue(source.getImages()))")
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternateNames", expression = "java(getAlternateNames(source))")
  @Mapping(target = "yearpublished", expression = "java(getFirstIntegerValue(source.getYearpublisheds()))")
  @Mapping(target = "productcode", expression = "java(getFirstStringValue(source.getProductcodes()))")
  @Mapping(target = "width", expression = "java(getFirstBigDecimalValue(source.getWidths()))")
  @Mapping(target = "length", expression = "java(getFirstBigDecimalValue(source.getLengths()))")
  @Mapping(target = "depth", expression = "java(getFirstBigDecimalValue(source.getDepths()))")
  @Mapping(target = "weight", expression = "java(getFirstBigDecimalValue(source.getWeights()))")
  Thing.ThingVersion fromBggModel(Version source);

  Thing.ThingVersion.ThingVersionLink fromBggModel(VersionLink source);

  Thing.ThingVideo fromBggModel(Video source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"price", "link"})
  @Mapping(target = "currency", expression = "java(source.getPrice().getCurrency())")
  @Mapping(target = "price", expression = "java(source.getPrice().getValue())")
  @Mapping(target = "link", expression = "java(source.getLink().getHref())")
  @Mapping(target = "title", expression = "java(source.getLink().getTitle())")
  Thing.ThingMarketplaceListing fromBggModel(Listing source);

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

  default String getName(com.boardgamegeek.thing.Thing source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(Name::getValue)
        .findFirst()
        .orElse(null);
  }

  default String getName(com.boardgamegeek.thing.Version source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(Name::getValue)
        .findFirst()
        .orElse(null);
  }

  default List<String> getAlternateNames(com.boardgamegeek.thing.Thing source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(Name::getValue)
        .collect(Collectors.toList());
  }

  default List<String> getAlternateNames(com.boardgamegeek.thing.Version source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(Name::getValue)
        .collect(Collectors.toList());
  }

  default List<Thing.ThingVideo> getVideos(com.boardgamegeek.thing.Thing source) {
    return source.getVideos().stream()
        .flatMap(e -> e.getVideo().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.ThingVersion> getVersions(com.boardgamegeek.thing.Thing source) {
    return source.getVersions().stream()
        .flatMap(e -> e.getItem().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.ThingMarketplaceListing> getMarketplaceListings(com.boardgamegeek.thing.Thing source) {
    return source.getMarketplacelistings().stream()
        .flatMap(e -> e.getListing().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default Thing.ThingStatistics getStats(com.boardgamegeek.thing.Thing source) {
    Thing.ThingStatistics target = new Thing.ThingStatistics();
    source.getComments().stream()
        .findFirst()
        .ifPresent(e -> {
          target.setRating(new Thing.ThingStatistics.ThingRating());
          target.getRating().setTotalcomments(e.getTotalitems());
          target.getRating().setComments(new ArrayList<>());
          e.getComment().forEach(c -> {
            Thing.ThingStatistics.ThingRating.ThingRatingComment comment = new Thing.ThingStatistics.ThingRating.ThingRatingComment();
            comment.setUsername(c.getUsername());
            comment.setValue(c.getValue());
            Optional.ofNullable(c.getRating())
                .filter(r -> !"N/A".equals(r))
                .map(BigDecimal::new)
                .ifPresent(comment::setRating);
            target.getRating().getComments().add(comment);
          });
        });
    source.getStatistics().stream()
        .findFirst()
        .flatMap(e -> e.getRatings().stream().findFirst())
        .ifPresent(e -> {
          Optional.ofNullable(e.getOwned()).ifPresent(v -> target.setOwned(v.getValue()));
          Optional.ofNullable(e.getTrading()).ifPresent(v -> target.setTrading(v.getValue()));
          Optional.ofNullable(e.getWanting()).ifPresent(v -> target.setWanting(v.getValue()));
          Optional.ofNullable(e.getWishing()).ifPresent(v -> target.setWishing(v.getValue()));
          Optional.ofNullable(e.getAverage()).ifPresent(v -> target.getRating().setAverage(v.getValue()));
          Optional.ofNullable(e.getBayesaverage()).ifPresent(v -> target.getRating().setBayesaverage(v.getValue()));
          Optional.ofNullable(e.getMedian()).ifPresent(v -> target.getRating().setMedian(v.getValue()));
          Optional.ofNullable(e.getStddev()).ifPresent(v -> target.getRating().setStddev(v.getValue()));
          Optional.ofNullable(e.getUsersrated()).ifPresent(v -> target.getRating().setUsersrated(v.getValue()));
          Optional.ofNullable(e.getNumcomments()).ifPresent(v -> target.getRating().setNumcomments(v.getValue()));
          Optional.ofNullable(source.getComments().iterator().next().getTotalitems()).ifPresent(v -> target.getRating().setTotalcomments(v));
          // weight
          if (e.getNumweights() != null || e.getAverageweight() != null) {
            target.setWeight(new Thing.ThingStatistics.ThingWeight());
            Optional.ofNullable(e.getNumweights()).ifPresent(v -> target.getWeight().setNumweights(v.getValue()));
            Optional.ofNullable(e.getAverageweight()).ifPresent(v -> target.getWeight().setAverageweight(v.getValue()));
          }
          // ranks
          Optional.ofNullable(e.getRanks()).map(Ranks::getRank).ifPresent(l -> {
            if (!l.isEmpty()) {
              target.setRanks(new ArrayList<>());
            }
            l.forEach(r -> {
              Thing.ThingStatistics.ThingRank rank = new Thing.ThingStatistics.ThingRank();
              rank.setId(r.getId());
              rank.setName(r.getName());
              rank.setFriendlyname(r.getFriendlyname());
              rank.setBayesaverage(r.getBayesaverage());
              rank.setType(r.getType());
              rank.setValue(r.getValue());
              target.getRanks().add(rank);
            });
          });
        });
    source.getPolls().stream()
        .filter(e -> "suggested_numplayers".equals(e.getName()))
        .findFirst()
        .ifPresent(p -> {
          target.setPlayerCount(new Thing.ThingStatistics.ThingPlayerCount());
          target.getPlayerCount().setResults(new ArrayList<>());
          p.getResults().forEach(r -> {
            Thing.ThingStatistics.ThingPlayerCount.ThingSuggestedPlayerCountResult result = new Thing.ThingStatistics.ThingPlayerCount.ThingSuggestedPlayerCountResult();
            result.setValue(r.getNumplayers());
            r.getResult().stream().filter(e -> "Best".equals(e.getValue())).map(Result::getNumvotes).findFirst().ifPresent(result::setNbBestVotes);
            r.getResult().stream().filter(e -> "Recommended".equals(e.getValue())).map(Result::getNumvotes).findFirst().ifPresent(result::setNbRecommendedVotes);
            r.getResult().stream().filter(e -> "Not Recommended".equals(e.getValue())).map(Result::getNumvotes).findFirst().ifPresent(result::setNbNotRecommendedVotes);
            target.getPlayerCount().setTotalvotes(p.getTotalvotes());
            target.getPlayerCount().getResults().add(result);
          });
        });
    source.getPolls().stream()
        .filter(e -> "suggested_playerage".equals(e.getName()))
        .findFirst()
        .ifPresent(p -> {
          target.setPlayerAge(new Thing.ThingStatistics.ThingPlayerAge());
          target.getPlayerAge().setResults(new ArrayList<>());
          p.getResults().iterator().next().getResult().forEach(r -> {
            Thing.ThingStatistics.ThingPlayerAge.ThingSuggestedPlayerAgeResult result = new Thing.ThingStatistics.ThingPlayerAge.ThingSuggestedPlayerAgeResult();
            result.setValue(r.getValue());
            result.setNumvotes(r.getNumvotes());
            target.getPlayerAge().setTotalvotes(p.getTotalvotes());
            target.getPlayerAge().getResults().add(result);
          });
        });
    source.getPolls().stream()
        .filter(e -> "language_dependence".equals(e.getName()))
        .findFirst()
        .ifPresent(p -> {
          target.setLanguageDependancy(new Thing.ThingStatistics.ThingLanguageDependency());
          target.getLanguageDependancy().setResults(new ArrayList<>());
          p.getResults().iterator().next().getResult().forEach(r -> {
            Thing.ThingStatistics.ThingLanguageDependency.ThingLanguageDependencyResult result = new Thing.ThingStatistics.ThingLanguageDependency.ThingLanguageDependencyResult();
            result.setLevel(r.getLevel());
            result.setValue(r.getValue());
            result.setNumvotes(r.getNumvotes());
            target.getLanguageDependancy().setTotalvotes(p.getTotalvotes());
            target.getLanguageDependancy().getResults().add(result);
          });
        });
    return target;
  }

  default <T> T getFirstValue(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
    Iterator<T> iterator = iterable.iterator();
    return iterator.hasNext() ? iterator.next() : null;
  }

  default String getFirstStringValue(Iterable<StringValue> iterable) {
    StringValue stringValue = getFirstValue(iterable);
    return stringValue != null ? stringValue.getValue() : null;
  }

  default Integer getFirstIntegerValue(Iterable<IntegerValue> iterable) {
    IntegerValue integerValue = getFirstValue(iterable);
    return integerValue != null ? integerValue.getValue() : null;
  }

  default BigDecimal getFirstBigDecimalValue(Iterable<DecimalValue> iterable) {
    DecimalValue decimalValue = getFirstValue(iterable);
    return decimalValue != null ? decimalValue.getValue() : null;
  }

  default LocalDate getFirstLocalDateValue(Iterable<LocalDateValue> iterable) {
    LocalDateValue localDateValue = getFirstValue(iterable);
    return localDateValue != null ? localDateValue.getValue() : null;
  }

  default LocalDateTime getFirstLocalDateTimeValue(Iterable<LocalDateTimeValue> iterable) {
    LocalDateTimeValue localDateTimeValue = getFirstValue(iterable);
    return localDateTimeValue != null ? localDateTimeValue.getValue() : null;
  }

}
