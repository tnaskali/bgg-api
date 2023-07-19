package li.naska.bgg.resource.vN.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SearchParams {

  @NotNull
  private String query;

  private List<String> type;

  private Boolean exact;

}
