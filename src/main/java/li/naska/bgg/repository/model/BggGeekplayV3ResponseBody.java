package li.naska.bgg.repository.model;

import lombok.Data;

@Data
public class BggGeekplayV3ResponseBody {

  /**
   * Only action=delete
   */
  private Boolean success;

  /**
   * Only action=save
   */
  private String playid;

  /**
   * Only action=save
   */
  private Integer numplays;

  private String html;

  private String error;
}
