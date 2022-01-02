package li.naska.bgg.mapper;

import com.boardgamegeek.common.IntegerValue;
import com.boardgamegeek.enums.NameType;
import li.naska.bgg.resource.v3.model.Collection;
import li.naska.bgg.resource.v3.model.Name;
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
  @Mapping(target = "name", expression = "java(getName(source))")
  @Mapping(target = "version", expression = "java(getVersion(source))")
  Collection.Item fromBggModel(com.boardgamegeek.collection.Item source);

  Collection.Item.Stats fromBggModel(com.boardgamegeek.collection.Stats source);

  @BeanMapping(ignoreUnmappedSourceProperties = {"ranks"})
  @Mapping(target = "ranks", expression = "java(getRanks(source))")
  Collection.Item.Stats.Rating fromBggModel(com.boardgamegeek.collection.Rating source);

  default List<Collection.Item.Stats.Rating.Rank> getRanks(com.boardgamegeek.collection.Rating source) {
    return Optional.ofNullable(source.getRanks())
        .map(o -> o.getRank().stream()
            .map(this::fromBggModel)
            .collect(Collectors.toList()))
        .orElse(null);
  }

  Collection.Item.Stats.Rating.Rank fromBggModel(com.boardgamegeek.collection.Rank source);

  Collection.Item.Status fromBggModel(com.boardgamegeek.collection.Status source);

  @Mapping(target = "pp_currency", source = "ppCurrency")
  @Mapping(target = "cv_currency", source = "cvCurrency")
  Collection.Item.PrivateInfo fromBggModel(com.boardgamegeek.collection.PrivateInfo source);

  default Collection.Item.Version getVersion(com.boardgamegeek.collection.Item source) {
    com.boardgamegeek.collection.Version version = source.getVersion();
    if (version == null) {
      return null;
    }
    Collection.Item.Version target = new Collection.Item.Version();
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
      target.setAlternatenames(getAlternatenames(o));
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

  Collection.Item.Version.Publisher fromBggModel(com.boardgamegeek.collection.VersionPublisher source);

  Collection.Item.Version.VersionLink fromBggModel(com.boardgamegeek.collection.VersionLink source);

  default Name getName(com.boardgamegeek.collection.Item source) {
    return Optional.ofNullable(source.getName())
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .orElse(null);
  }

  default Name getName(com.boardgamegeek.collection.VersionItem source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.primary)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default List<Name> getAlternatenames(com.boardgamegeek.collection.VersionItem source) {
    return source.getNames().stream()
        .filter(e -> e.getType() == NameType.alternate)
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

}
