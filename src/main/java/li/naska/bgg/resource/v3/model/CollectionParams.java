package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.CollectionItemSubtype;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CollectionParams {

  @NotNull
  private String username;

  private Boolean version;

  private CollectionItemSubtype subtype;

  private List<Integer> id;

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

  private Integer minrating;

  private Integer maxrating;

  private Integer minbggrating;

  private Integer maxbggrating;

  private Integer minplays;

  private Integer maxplays;

  private Boolean showprivate;

  private Integer collid;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate modifiedsincedate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime modifiedsincetime;

}
