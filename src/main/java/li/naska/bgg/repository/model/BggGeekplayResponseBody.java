package li.naska.bgg.repository.model;

import lombok.Data;

@Data
public class BggGeekplayResponseBody {

  private Boolean success;

  private String url;

  private String playid;

  private Integer numplays;

  private String html;

  private String error;

}
