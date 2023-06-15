package li.naska.bgg.mapper;

import com.boardgamegeek.collection.VersionLink;
import com.boardgamegeek.common.DecimalValue;
import com.boardgamegeek.common.IntegerValue;
import com.boardgamegeek.common.StringValue;
import jakarta.xml.bind.JAXBElement;
import li.naska.bgg.resource.vN.model.Collection;
import li.naska.bgg.resource.vN.model.Name;
import org.mapstruct.*;

import java.math.BigDecimal;
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

  @BeanMapping(ignoreUnmappedSourceProperties = {"termsofuse"})
  Collection fromBggModel(com.boardgamegeek.collection.Items source);

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
        .map(o -> o.getRanks().stream()
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
      target.setYearpublished(getYearpublished(o));
      target.setProductcode(getProductcode(o));
      target.setWidth(getWidth(o));
      target.setLength(getLength(o));
      target.setDepth(getDepth(o));
      target.setWeight(getWeight(o));
      target.setThumbnail(getThumbnail(o));
      target.setImage(getImage(o));
      target.setLinks(getLinks(o));
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
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "name".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.collection.VersionName) e)
        .filter(e -> "primary".equals(e.getType()))
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .findFirst()
        .orElse(null);
  }

  default List<Name> getAlternatenames(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "name".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (com.boardgamegeek.collection.VersionName) e)
        .filter(e -> "alternate".equals(e.getType()))
        .map(e -> new Name(e.getValue(), e.getSortindex()))
        .collect(Collectors.toList());
  }

  default Integer getYearpublished(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "yearpublished".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (IntegerValue) e)
        .map(IntegerValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default String getProductcode(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "productcode".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (StringValue) e)
        .map(StringValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getWidth(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "width".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getLength(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "length".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getWeight(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "weight".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default BigDecimal getDepth(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "depth".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (DecimalValue) e)
        .map(DecimalValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default String getImage(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "image".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (String) e)
        .findFirst()
        .orElse(null);
  }

  default String getThumbnail(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "thumbnail".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (StringValue) e)
        .map(StringValue::getValue)
        .findFirst()
        .orElse(null);
  }

  default List<Collection.Item.Version.VersionLink> getLinks(com.boardgamegeek.collection.VersionItem source) {
    return source.getThumbnailsAndImagesAndNames().stream()
        .filter(e -> "link".equals(e.getName().getLocalPart()))
        .map(JAXBElement::getValue)
        .map(e -> (VersionLink) e)
        .map(this::fromBggModel)
        .collect(Collectors.toList());
  }

}
