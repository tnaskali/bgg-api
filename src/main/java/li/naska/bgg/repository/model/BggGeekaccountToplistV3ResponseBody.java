package li.naska.bgg.repository.model;

import lombok.Data;

@Data
public class BggGeekaccountToplistV3ResponseBody {
  private String error;
  private String id;
  private String ajax;
  private String action;
  private String listtype;
  private String domain;
  private String userid;
  private Boolean maxitems;
  private String html;
}
