package li.naska.bgg.repository.model;

import lombok.Data;

@Data
public class BggGeekplayV3ResponseBody {

  /**
   * Only action=delete
   */
  private Boolean success;

  /**
   * Only action=delete
   */
  private String playid;

  /**
   * Only action=delete
   */
  private Integer numplays;

  /**
   * Only action=delete
   */
  private String html;

  /**
   * Only action=getuserplaycount
   */
  private Integer userid;

  /**
   * Only action=getuserplaycount
   */
  private Integer count;

  private String error;

}
