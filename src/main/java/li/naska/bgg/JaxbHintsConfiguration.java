package li.naska.bgg;

import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.*;

import java.util.Map;

/**
 * JAXB cannot be configured automatically because of its factory method being static and taking a class array as
 * one of its parameters.
 *
 * @see com.sun.xml.bind.v2.ContextFactory#createContext(java.lang.Class[], java.util.Map)
 * @see <a href="https://www.graalvm.org/22.0/reference-manual/native-image/Reflection/#automatic-detection">reflection automatic detection</a>
 */
@Configuration
@NativeHint(
    jdkProxies = {
        // @see com.sun.xml.bind.v2.model.annotation.LocatableAnnotation#create
        @JdkProxyHint(
            typeNames = {
                "javax.xml.bind.annotation.XmlAccessorType",
                "com.sun.xml.bind.v2.model.annotation.Locatable"
            }),
        @JdkProxyHint(
            typeNames = {
                "javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter",
                "com.sun.xml.bind.v2.model.annotation.Locatable"
            })
    },
    types = {
        @TypeHint(
            typeNames = {
                "com.boardgamegeek.collection.Collection",
                "com.boardgamegeek.collection.Item",
                "com.boardgamegeek.collection.Name",
                "com.boardgamegeek.collection.PrivateInfo",
                "com.boardgamegeek.collection.Rank",
                "com.boardgamegeek.collection.Ranks",
                "com.boardgamegeek.collection.Rating",
                "com.boardgamegeek.collection.Stats",
                "com.boardgamegeek.collection.Status",
                "com.boardgamegeek.collection.Version",
                "com.boardgamegeek.collection.VersionItem",
                "com.boardgamegeek.collection.VersionLink",
                "com.boardgamegeek.collection.VersionPublisher",
                "com.boardgamegeek.common.DecimalValue",
                "com.boardgamegeek.common.IntegerValue",
                "com.boardgamegeek.common.LocalDateTimeValue",
                "com.boardgamegeek.common.LocalDateValue",
                "com.boardgamegeek.common.SortIndexString",
                "com.boardgamegeek.common.StringValue",
                "com.boardgamegeek.common.ZonedDateTimeValue",
                "com.boardgamegeek.enums.CollectionItemSubtype",
                "com.boardgamegeek.enums.FamilyLinkType",
                "com.boardgamegeek.enums.FamilyType",
                "com.boardgamegeek.enums.HotItemType",
                "com.boardgamegeek.enums.ItemSubtype",
                "com.boardgamegeek.enums.ItemType",
                "com.boardgamegeek.enums.NameType",
                "com.boardgamegeek.enums.RankType",
                "com.boardgamegeek.enums.SearchType",
                "com.boardgamegeek.enums.SortType",
                "com.boardgamegeek.enums.ThingLinkType",
                "com.boardgamegeek.enums.ThingType",
                "com.boardgamegeek.enums.UserDomainType",
                "com.boardgamegeek.enums.VersionLinkType",
                "com.boardgamegeek.enums.VersionType",
                "com.boardgamegeek.family.Families",
                "com.boardgamegeek.family.Family",
                "com.boardgamegeek.family.Link",
                "com.boardgamegeek.family.Name",
                "com.boardgamegeek.forum.Forum",
                "com.boardgamegeek.forum.Thread",
                "com.boardgamegeek.forum.Threads",
                "com.boardgamegeek.forumlist.Forum",
                "com.boardgamegeek.forumlist.Forums",
                "com.boardgamegeek.geeklist.Comment",
                "com.boardgamegeek.geeklist.Geeklist",
                "com.boardgamegeek.geeklist.Item",
                "com.boardgamegeek.guild.Guild",
                "com.boardgamegeek.guild.Location",
                "com.boardgamegeek.guild.Member",
                "com.boardgamegeek.guild.Members",
                "com.boardgamegeek.hot.HotItems",
                "com.boardgamegeek.hot.HotItem",
                "com.boardgamegeek.plays.Item",
                "com.boardgamegeek.plays.Play",
                "com.boardgamegeek.plays.Player",
                "com.boardgamegeek.plays.Players",
                "com.boardgamegeek.plays.Plays",
                "com.boardgamegeek.plays.Subtype",
                "com.boardgamegeek.plays.SubtypeValue",
                "com.boardgamegeek.search.Name",
                "com.boardgamegeek.search.Result",
                "com.boardgamegeek.search.Results",
                "com.boardgamegeek.thing.Comment",
                "com.boardgamegeek.thing.Comments",
                "com.boardgamegeek.thing.Link",
                "com.boardgamegeek.thing.Listing",
                "com.boardgamegeek.thing.ListingLink",
                "com.boardgamegeek.thing.ListingPrice",
                "com.boardgamegeek.thing.Marketplacelistings",
                "com.boardgamegeek.thing.Name",
                "com.boardgamegeek.thing.Poll",
                "com.boardgamegeek.thing.Rank",
                "com.boardgamegeek.thing.Ranks",
                "com.boardgamegeek.thing.Ratings",
                "com.boardgamegeek.thing.Result",
                "com.boardgamegeek.thing.Results",
                "com.boardgamegeek.thing.Statistics",
                "com.boardgamegeek.thing.Thing",
                "com.boardgamegeek.thing.Things",
                "com.boardgamegeek.thing.Version",
                "com.boardgamegeek.thing.VersionLink",
                "com.boardgamegeek.thing.Versions",
                "com.boardgamegeek.thing.Video",
                "com.boardgamegeek.thing.Videos",
                "com.boardgamegeek.thread.Article",
                "com.boardgamegeek.thread.Articles",
                "com.boardgamegeek.thread.Thread",
                "com.boardgamegeek.user.Buddies",
                "com.boardgamegeek.user.Buddy",
                "com.boardgamegeek.user.Guild",
                "com.boardgamegeek.user.Guilds",
                "com.boardgamegeek.user.Ranking",
                "com.boardgamegeek.user.RankingItem",
                "com.boardgamegeek.user.User"
            },
            access = {
                TypeAccess.PUBLIC_CONSTRUCTORS,
                TypeAccess.DECLARED_FIELDS
            }),
        @TypeHint(
            typeNames = {
                "li.naska.bgg.util.DateTimeToZonedDateTimeAdapter",
                "li.naska.bgg.util.IntegerToIntegerAdapter",
                "li.naska.bgg.util.StringToLocalDateAdapter",
                "li.naska.bgg.util.StringToLocalDateTimeAdapter",
                "li.naska.bgg.util.StringToZonedDateTimeAdapter"
            },
            access = {
                TypeAccess.DECLARED_CONSTRUCTORS
            }),
        // @see javax.xml.bind.ContextFinder.newInstance(java.lang.Class[], java.util.Map, java.lang.Class)
        @TypeHint(
            typeNames = {
                "com.sun.xml.bind.v2.ContextFactory"
            },
            methods = @MethodHint(
                name = "createContext",
                parameterTypes = {Class[].class, Map.class}
            ),
            access = {
                TypeAccess.PUBLIC_METHODS
            }),
        @TypeHint(
            typeNames = {
                "com.sun.xml.bind.v2.model.nav.ReflectionNavigator"
            },
            methods = @MethodHint(
                name = "getInstance"
            ),
            access = {
                TypeAccess.DECLARED_METHODS
            }),
        @TypeHint(
            typeNames = {
                "com.sun.xml.bind.v2.runtime.property.ArrayElementLeafProperty",
                "com.sun.xml.bind.v2.runtime.property.ArrayElementNodeProperty",
                "com.sun.xml.bind.v2.runtime.property.ArrayReferenceNodeProperty",
                "com.sun.xml.bind.v2.runtime.property.SingleElementLeafProperty",
                "com.sun.xml.bind.v2.runtime.property.SingleElementNodeProperty",
                "com.sun.xml.bind.v2.runtime.property.SingleMapNodeProperty",
                "com.sun.xml.bind.v2.runtime.property.SingleReferenceNodeProperty"
            },
            access = {
                TypeAccess.PUBLIC_CONSTRUCTORS
            }),
        @TypeHint(
            typeNames = {
                // "javax.xml.bind.annotation.XmlAccessorOrder",
                "javax.xml.bind.annotation.XmlAccessorType",
                // "javax.xml.bind.annotation.XmlAnyAttribute",
                // "javax.xml.bind.annotation.XmlAnyElement",
                // "javax.xml.bind.annotation.XmlAttachmentRef",
                "javax.xml.bind.annotation.XmlAttribute",
                "javax.xml.bind.annotation.XmlElement",
                // "javax.xml.bind.annotation.XmlElementDecl",
                // "javax.xml.bind.annotation.XmlElementRef",
                // "javax.xml.bind.annotation.XmlElementRefs",
                // "javax.xml.bind.annotation.XmlElements",
                // "javax.xml.bind.annotation.XmlElementWrapper",
                "javax.xml.bind.annotation.XmlEnum",
                // "javax.xml.bind.annotation.XmlEnumValue",
                // "javax.xml.bind.annotation.XmlID",
                // "javax.xml.bind.annotation.XmlIDREF",
                // "javax.xml.bind.annotation.XmlInlineBinaryData",
                // "javax.xml.bind.annotation.XmlList",
                // "javax.xml.bind.annotation.XmlMimeType",
                // "javax.xml.bind.annotation.XmlMixed",
                // "javax.xml.bind.annotation.XmlNs",
                // "javax.xml.bind.annotation.XmlRegistry",
                // "javax.xml.bind.annotation.XmlRootElement",
                // "javax.xml.bind.annotation.XmlSchema",
                "javax.xml.bind.annotation.XmlSchemaType",
                // "javax.xml.bind.annotation.XmlSchemaTypes",
                // "javax.xml.bind.annotation.XmlSeeAlso",
                // "javax.xml.bind.annotation.XmlTransient",
                "javax.xml.bind.annotation.XmlType",
                // "javax.xml.bind.annotation.XmlValue",
                "javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter"
                // "javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters"
            },
            access = {
                TypeAccess.PUBLIC_CONSTRUCTORS,
                TypeAccess.PUBLIC_METHODS
            })
    }
)
public class JaxbHintsConfiguration {
}
