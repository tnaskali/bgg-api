package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BggGeekplayResponseBody {

  private Boolean success;

  private String url;

  private String playid;

  private Integer numplays;

  private String html;

  private String error;

}
