package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class BggUserItemsParameters {

  private final String username;

  private Boolean version;

  private ObjectSubtype subtype;

  private ObjectSubtype excludesubtype;

  private List<Integer> ids;

  private Boolean brief;

  private Boolean stats;

  private Boolean own;

  private Boolean rated;

  private Boolean played;

  private Boolean comment;

  private Boolean trade;

  private Boolean want;

  private Boolean wishlist;

  private Integer wishlistpriority;

  private Boolean preordered;

  private Boolean wanttoplay;

  private Boolean wanttobuy;

  private Boolean prevowned;

  private Boolean hasparts;

  private Boolean wantparts;

  private BigDecimal minrating;

  private BigDecimal maxrating;

  private BigDecimal minbggrating;

  private BigDecimal maxbggrating;

  private Integer minplays;

  private Integer maxplays;

  private Boolean showprivate;

  private Integer collid;

  private LocalDate modifiedsincedate;

  private LocalTime modifiedsincetime;

}
