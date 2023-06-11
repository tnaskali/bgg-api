package li.naska.bgg.mapper;

import com.boardgamegeek.common.DecimalValue;
import com.boardgamegeek.common.IntegerValue;
import com.boardgamegeek.common.LocalDateValue;
import com.boardgamegeek.common.StringValue;
import com.boardgamegeek.thing.*;
import jakarta.xml.bind.JAXBElement;
import li.naska.bgg.resource.vN.model.Name;
import li.naska.bgg.resource.vN.model.Thing;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
      "thumbnailsAndImagesAndNames"
  })
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternatenames", expression = "java(getAlternatenames(source))")
  @Mapping(target = "thumbnail", expression = "java(getThumbnail(source))")
  @Mapping(target = "image", expression = "java(getImage(source))")
  @Mapping(target = "description", expression = "java(getDescription(source))")
  @Mapping(target = "yearpublished", expression = "java(getYearpublished(source))")
  @Mapping(target = "datepublished", expression = "java(getDatepublished(source))")
  @Mapping(target = "releasedate", expression = "java(getReleasedate(source))")
  @Mapping(target = "seriescode", expression = "java(getSeriescode(source))")
  @Mapping(target = "issueindex", expression = "java(getIssueIndex(source))")
  @Mapping(target = "minplayers", expression = "java(getMinplayers(source))")
  @Mapping(target = "maxplayers", expression = "java(getMaxplayers(source))")
  @Mapping(target = "playingtime", expression = "java(getPlayingtime(source))")
  @Mapping(target = "minplaytime", expression = "java(getMinplaytime(source))")
  @Mapping(target = "maxplaytime", expression = "java(getMaxplaytime(source))")
  @Mapping(target = "minage", expression = "java(getMinage(source))")
  @Mapping(target = "videos", expression = "java(getVideos(source))")
  @Mapping(target = "numcomments", expression = "java(getNumcomments(source))")
  @Mapping(target = "comments", expression = "java(getComments(source))")
  @Mapping(target = "versions", expression = "java(getVersions(source))")
  @Mapping(target = "statistics", expression = "java(getStatistics(source))")
  @Mapping(target = "marketplacelistings", expression = "java(getMarketplaceListings(source))")
  @Mapping(target = "links", expression = "java(getLinks(source))")
  @Mapping(target = "polls", expression = "java(getPolls(source))")
  Thing fromBggModel(com.boardgamegeek.thing.Thing source);

  default Thing.Statistics getStatistics(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "statistics".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (Statistics) e)
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
            Function<String, Integer> votesFunction = (name) -> r.getResults().stream()
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
          .flatMap(e -> e.getResults().stream())
          .map(this::fromBggModel)
          .collect(Collectors.toList());
    }
  }

  @BeanMapping(ignoreUnmappedSourceProperties = {
      "thumbnailsAndImagesAndNames"
  })
  @Mapping(target = "thumbnail", expression = "java(getThumbnail(source))")
  @Mapping(target = "image", expression = "java(getImage(source))")
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "alternatenames", expression = "java(getAlternatenames(source))")
  @Mapping(target = "yearpublished", expression = "java(getYearpublished(source))")
  @Mapping(target = "productcode", expression = "java(getProductcode(source))")
  @Mapping(target = "width", expression = "java(getWidth(source))")
  @Mapping(target = "length", expression = "java(getLength(source))")
  @Mapping(target = "depth", expression = "java(getDepth(source))")
  @Mapping(target = "weight", expression = "java(getWeight(source))")
  @Mapping(target = "links", expression = "java(getLinks(source))")
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
        .map(o -> o.getRanks().stream()
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

  default String getImage(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "image".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (String) e)
        .findFirst()
        .orElse(null);
  }

  default String getImage(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "image".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (String) e)
        .findFirst()
        .orElse(null);
  }

  default String getThumbnail(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "thumbnail".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (String) e)
        .findFirst()
        .orElse(null);
  }

  default String getThumbnail(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "thumbnail".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (String) e)
        .findFirst()
        .orElse(null);
  }

  default String getDescription(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "description".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (String) e)
        .findFirst()
        .orElse(null);
  }

  default Integer getYearpublished(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "yearpublished".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getYearpublished(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "yearpublished".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default LocalDate getDatepublished(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "datepublished".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (StringValue) e)
        .map(StringValue::getValue)
        .flatMap(e -> {
          try {
            return Stream.of(LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(e)));
          } catch (DateTimeException dte) {
            return Stream.empty();
          }
        }).findFirst()
        .orElse(null);
  }

  default LocalDate getReleasedate(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "releasedate".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (LocalDateValue) e)
        .map(LocalDateValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default String getSeriescode(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "seriescode".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (StringValue) e)
        .map(StringValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getIssueIndex(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "issueIndex".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getMinplayers(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "minplayers".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getMaxplayers(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "maxplayers".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getPlayingtime(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "playingtime".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getMinplaytime(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "minplaytime".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getMaxplaytime(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "maxplaytime".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Integer getMinage(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "minage".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default Name getName(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "name".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Name) e)
        .filter(e -> "primary".equals(e.getType()))
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default Name getName(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "name".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Name) e)
        .filter(e -> "primary".equals(e.getType()))
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default List<Name> getAlternatenames(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "name".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Name) e)
        .filter(e -> "alternate".equals(e.getType()))
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

  default List<Name> getAlternatenames(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "name".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Name) e)
        .filter(e -> "alternate".equals(e.getType()))
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

  default String getProductcode(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "productcode".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (StringValue) e)
        .map(StringValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getWidth(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "width".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getLength(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "length".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getWeight(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "weight".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getDepth(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "depth".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default List<Thing.Link> getLinks(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "link".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (Link) e)
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.Version.VersionLink> getLinks(com.boardgamegeek.thing.Version source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "link".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (VersionLink) e)
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.Video> getVideos(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "videos".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Videos) e)
        .flatMap(e -> e.getVideos().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.Version> getVersions(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "versions".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Versions) e)
        .flatMap(e -> e.getItems().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default Integer getNumcomments(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "comments".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Comments) e)
        .findFirst()
        .map(Comments::getTotalitems)
        .orElse(null);
  }

  default List<Thing.Comment> getComments(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "comments".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Comments) e)
        .flatMap(e -> e.getComments().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.MarketplaceListing> getMarketplaceListings(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "marketplacelistings".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Marketplacelistings) e)
        .flatMap(e -> e.getListings().stream())
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

  default List<Thing.Poll> getPolls(com.boardgamegeek.thing.Thing source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "poll".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.thing.Poll) e)
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

}
