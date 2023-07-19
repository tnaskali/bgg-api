package li.naska.bgg.resource.vN.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FamiliesParams {

  @NotNull
  private List<Integer> id;

  private List<String> type;

}
