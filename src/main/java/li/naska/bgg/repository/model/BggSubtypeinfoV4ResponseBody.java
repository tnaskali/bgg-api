package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import lombok.Data;

@Data
public class BggSubtypeinfoV4ResponseBody {
  private Item item;

  @Data
  public static class Item {
    private List<ItemData> itemdata;
    private List<String> relatedlinktypes;
    private List<String> classification_types;
    private List<LinkedForumType> linkedforum_types;
    private String subtypename;
    private List<String> hide_collection_fields;
    private String honor_subtype;
    private String honor_linktype;
    private List<ItemData> credit_subtypes;
    private Boolean show_geekbuddy_analysis;

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "datatype")
    @JsonSubTypes({
      @JsonSubTypes.Type(value = FieldData.class, name = "geekitem_fielddata"),
      @JsonSubTypes.Type(value = LinkData.class, name = "geekitem_linkdata"),
      @JsonSubTypes.Type(value = PollData.class, name = "geekitem_polldata")
    })
    public sealed interface ItemData permits FieldData, LinkData, PollData {}

    @Data
    public static final class FieldData implements ItemData {
      private String datatype;
      private String fieldname;
      private String title;
      private String shorttitle;
      private String createtitle;
      private String createnote;
      private String posttext;
      private String createposttext;
      private String validatemethod;
      private String correctioncomment;
      private String table;
      private Integer maxlength;
      private Integer editfieldsize;
      private String unittype;
      private String displaytype;
      private String string_format;
      private String editwidth;
      private String altfieldname;
      private List<String> savefields;
      private List<Option> options;
      private Boolean primaryname;
      private Boolean alternate;
      private Boolean required;
      private Boolean requireint;
      private Boolean nullable;
      private Boolean unclickable;
      private Boolean fullcredits;
      private Boolean adminonly;
      private Boolean checkbox;
      private Boolean date;
      private Boolean color;
      private Boolean invlinks;
      private Boolean revalidate_on_save;
      private Boolean disable_on_override;
      private RequiredAlt requiredalt;
      private TextArea textarea;
      private String dateprefix;
      private String overridefield;
      private String requiredoverride;
      private String subtype;
      private String keyname;
      /** can be a String or a List<String> */
      private Object adminaccess;

      @Data
      public static class Option {
        private Object value;
        private String title;
        private String length;
        private String height;
        private String width;
        private String depth;
        private String note;
      }

      @Data
      public static class RequiredAlt {
        private String newname;
        private String length;
        private String height;
        private String width;
        private String depth;
      }

      @Data
      public static class TextArea {
        private String style;
      }
    }

    @Data
    public static final class LinkData implements ItemData {
      private String datatype;
      private String other_objecttype;
      private String other_subtype;
      private String lookup_subtype;
      private String linktype;
      private String polltype;
      private String self_prefix;
      private String correctioncomment;
      private String title;
      private String titlepl;
      private String shorttitle;
      private String createtitle;
      private String createsubtext;
      private String createposttext;
      private String chain_title;
      private List<String> chain_index;
      private Boolean other_is_dependent;
      private Boolean required;
      private Boolean loadlinks;
      private Boolean hidecontrols;
      private Boolean showall_ctrl;
      private Boolean showsearch;
      private Boolean display_inline;
      private Boolean fullcredits;
      private Boolean addnew;
      private Boolean addtoparent;
      private Boolean adminonly;
      private Boolean nameonly;
      private String error;
      private Schema schema;
      private Integer overview_count;
      private String keyname;

      @Data
      public static class Schema {
        private String itemprop;
        private String itemtype;
      }
    }

    @Data
    public static final class PollData implements ItemData {
      private String datatype;
      private String title;
      private String polltype;
      private String keyname;
    }

    @Data
    public static class LinkedForumType {
      private String title;
      private String linkedforum_index;
      private String linkdata_index;
      private String required_subtype;
    }
  }
}
