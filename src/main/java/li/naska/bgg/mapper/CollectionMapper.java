package li.naska.bgg.mapper;

import com.boardgamegeek.collection.Name;
import com.boardgamegeek.common.IntegerValue;
import com.boardgamegeek.enums.NameType;
import li.naska.bgg.resource.v3.model.Collection;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    uses = {
        IntegerValueToIntegerMapper.class,
        DecimalValueToBigDecimalMapper.class
    }
)
public interface CollectionMapper extends BaseMapper {

  @BeanMapping(ignoreUnmappedSourceProperties = {"item", "termsofuse"})
  @Mapping(target = "items", source = "item")
  Collection fromBggModel(com.boardgamegeek.collection.Collection source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"name", "version"})
  @Mapping(target = "name", expression = "java(source.getName().getValue())")
  @Mapping(target = "version", expression = "java(getVersion(source))")
  Collection.CollectionItem fromBggModel(com.boardgamegeek.collection.Item source);

  Collection.CollectionItem.CollectionItemStats fromBggModel(com.boardgamegeek.collection.Stats source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"ranks"})
  @Mapping(target = "ranks", expression = "java(getRanks(source))")
  Collection.CollectionItem.CollectionItemStats.CollectionItemStatsRating fromBggModel(com.boardgamegeek.collection.Rating source);

  default List<Collection.CollectionItem.CollectionItemStats.CollectionItemStatsRating.CollectionItemStatsRatingRank> getRanks(com.boardgamegeek.collection.Rating source) {
    return Optional.ofNullable(source.getRanks())
        .map(o -> o.getRank().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

  Collection.CollectionItem.CollectionItemStats.CollectionItemStatsRating.CollectionItemStatsRatingRank fromBggModel(com.boardgamegeek.collection.Rank source);

  Collection.CollectionItem.CollectionItemStatus fromBggModel(com.boardgamegeek.collection.Status source);

  Collection.CollectionItem.CollectionItemPrivateInfo fromBggModel(com.boardgamegeek.collection.PrivateInfo source);

  default Collection.CollectionItem.CollectionItemVersion getVersion(com.boardgamegeek.collection.Item source) {
    com.boardgamegeek.collection.Version version = source.getVersion();
    if (version == null) {
      return null;
    }
    Collection.CollectionItem.CollectionItemVersion target = new Collection.CollectionItem.CollectionItemVersion();
    target.setYear(version.getYear());
    target.setBarcode(version.getBarcode());
    target.setOther(version.getOther());
    Optional.ofNullable(version.getImageid())
        .map(IntegerValue::getValue)
        .ifPresent(target::setImageid);
    Optional.ofNullable(version.getPublisher())
        .map(this::fromBggModel)
        .ifPresent(target::setPublisher);
    Optional.ofNullable(version.getItem()).ifPresent(o -> {
      target.setId(o.getId());
      target.setType(o.getType());
      target.setName(getName(o));
      target.setAlternateNames(getAlternateNames(o));
      target.setYearpublished(getFirstIntegerValue(o.getYearpublisheds()));
      target.setProductcode(getFirstStringValue(o.getProductcodes()));
      target.setWidth(getFirstBigDecimalValue(o.getWidths()));
      target.setLength(getFirstBigDecimalValue(o.getLengths()));
      target.setDepth(getFirstBigDecimalValue(o.getDepths()));
      target.setWeight(getFirstBigDecimalValue(o.getWeights()));
      target.setThumbnail(getFirstValue(o.getThumbnails()));
      target.setImage(getFirstValue(o.getImages()));
      Optional.ofNullable(o.getLinks())
          .map(l -> l.stream()
              .map(this::fromBggModel)
              .collect(Collectors.toList()))
          .ifPresent(target::setLinks);
    });
    return target;
  }

  Collection.CollectionItem.CollectionItemVersion.CollectionItemVersionPublisher fromBggModel(com.boardgamegeek.collection.VersionPublisher source);

  Collection.CollectionItem.CollectionItemVersion.CollectionItemVersionLink fromBggModel(com.boardgamegeek.collection.VersionLink source);

  default String getName(com.boardgamegeek.collection.VersionItem source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(Name::getValue)
        .findFirst()
        .get();
  }

  default List<String> getAlternateNames(com.boardgamegeek.collection.VersionItem source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(Name::getValue)
        .collect(Collectors.toList());
  }

}
